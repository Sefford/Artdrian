package com.sefford.artdrian.downloads.domain.model

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import com.sefford.artdrian.common.data.DataError
import com.sefford.artdrian.common.data.network.HttpClient
import com.sefford.artdrian.common.data.toDataError
import com.sefford.artdrian.common.language.files.Size.Companion.kBs
import com.sefford.artdrian.common.language.fold
import com.sefford.artdrian.common.language.orElse
import com.sefford.artdrian.common.language.orFalse
import com.sefford.artdrian.downloads.store.DownloadsEvents
import com.sefford.artdrian.downloads.store.DownloadsState
import com.sefford.artdrian.downloads.store.DownloadsStore
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.readAvailable
import java.io.File

sealed class DownloadProcess {

    sealed class Step : DownloadProcess() {

        // Check for errors with Error.catch
        class Viability(
            private val client: HttpClient,
            private val downloads: DownloadsState,
            private val events: (DownloadsEvents) -> Unit,
            private val directory: File,
            private val id: String
        ) : Step() {

            constructor(
                client: HttpClient,
                downloads: DownloadsStore,
                directory: File,
                id: String
            ) : this(
                client = client,
                downloads = downloads.current,
                events = downloads::event,
                directory = directory,
                id = id
            )

            fun check(): Either<Results, Probe> {
                return when (downloads.viabilityOf(id)) {
                    DownloadsState.Viability.FAILURE -> Results.Failure.left()
                    DownloadsState.Viability.WAIT -> Results.Retry.left()
                    else -> downloads[id]
                        .toEither { Results.Failure }
                        .flatMap { download ->
                            when (download) {
                                is Download.Finished -> Results.Success.left()
                                else -> Probe(events, client, directory, download).right()
                            }
                        }
                }
            }
        }

        class Probe(
            private val events: (DownloadsEvents) -> Unit,
            private val client: HttpClient,
            private val directory: File,
            private val download: Download,
        ) : Step() {

            suspend fun analyze(): Either<Results, Prime> =
                Either.catch {
                    client.get(download.url) {
                        if (download is Download.Ongoing && download.progress > 0) {
                            header(HttpHeaders.Range, "bytes=${download.progress}-")
                        }
                    }
                }
                    .errorToResult()
                    .statusCheck()
                    .map { response -> Prime(events, client, directory, response, download) }
        }

        class Prime(
            private val events: (DownloadsEvents) -> Unit,
            private val client: HttpClient,
            private val directory: File,
            val response: HttpResponse,
            val download: Download
        ) : Step() {

            suspend fun ready(): Either<Results, Fetch> =
                when (download) {
                    is Download.Pending -> Fetch(
                        events,
                        response.status,
                        response.bodyAsChannel(),
                        download.start(response.headers, directory)
                    ).right()

                    is Download.Ongoing -> download.confirm()
                    is Download.Finished -> Results.Success.left()
                }

            private suspend fun Download.Ongoing.confirm(): Either<Results, Fetch> {
                val refresh = refresh(response.headers, directory)
                return if (this == refresh) {
                    Fetch(events, response.status, response.bodyAsChannel(), this).right()
                } else {
                    Either.catch { client.get(url) }
                        .errorToResult()
                        .statusCheck()
                        .map { response -> Fetch(events, response.status, response.bodyAsChannel(), refresh) }
                }
            }
        }

        class Fetch(
            private val events: (DownloadsEvents) -> Unit,
            private val status: HttpStatusCode,
            private val body: ByteReadChannel,
            private val download: Download.Ongoing
        ) : Step() {

            suspend fun start(): Results {
                events(DownloadsEvents.Update(download))
                return Either.catch {
                    if (status == HttpStatusCode.OK) {
                        download.clear()
                    }
                    download()
                    Results.Success
                }
                    .errorToResult()
                    .fold { it }
            }

            private suspend fun download() {
                download.sink.use { sink ->
                    val buffer = ByteArray(8.kBs.inBytes.toInt())
                    while (!body.isClosedForRead) {
                        val bytesRead = body.readAvailable(buffer)
                        if (bytesRead > 0) {
                            sink.write(buffer, 0, bytesRead)
                        }
                    }
                }
            }
        }
    }

    protected fun <T> Either<Throwable, T>.errorToResult() =
        toDataError()
            .mapLeft { error ->
                when (error) {
                    DataError.Network.ConnectTimeout,
                    DataError.Network.SocketTimeout,
                    DataError.Network.NoConnection -> Results.Retry

                    else -> Results.Failure
                }
            }

    protected fun Either<Results, HttpResponse>.statusCheck() = flatMap { response ->
        if (response.status.ok) {
            response.right()
        } else {
            response.status.result.left()
        }
    }

    protected val HttpStatusCode?.result: Results
        get() = this?.value?.let { code ->
            when (code) {
                in 500..504, 408 -> Results.Retry
                else -> Results.Failure
            }
        }.orElse { Results.Failure }

    protected val HttpStatusCode?.ok: Boolean
        get() = this?.let { code ->
            code == HttpStatusCode.OK || code == HttpStatusCode.PartialContent
        }.orFalse()

    sealed class Results : DownloadProcess() {

        data object Failure : Results()

        data object Retry : Results()

        data object Success : Results()

    }
}
