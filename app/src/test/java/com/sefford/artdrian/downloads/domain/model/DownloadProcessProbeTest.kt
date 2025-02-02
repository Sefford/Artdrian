package com.sefford.artdrian.downloads.domain.model

import com.sefford.artdrian.test.FakeLogger
import com.sefford.artdrian.test.networking.FakeHttpClient
import com.sefford.artdrian.test.InjectableTest
import com.sefford.artdrian.test.networking.LazyMockEngineHandler
import com.sefford.artdrian.test.mothers.DownloadsMother
import com.sefford.artdrian.test.networking.respondOnly
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.ktor.client.HttpClient
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headers
import kotlinx.coroutines.test.runTest
import okio.buffer
import okio.sink
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.channels.UnresolvedAddressException
import java.nio.file.Files
import javax.inject.Inject

class DownloadProcessProbeTest : InjectableTest() {

    private val cache = Files.createTempDirectory("downloads").toFile()

    @Inject
    internal lateinit var client: HttpClient

    @Inject
    internal lateinit var handlers: LazyMockEngineHandler

    private val logger = FakeLogger()

    @BeforeEach
    override fun beforeEach() {
        super.beforeEach()
        graph.inject(this)
    }

    @Test
    fun `returns failure when received an unexpected exception`() = runTest {
        givenAProbe(FakeHttpClient(client, { throw IllegalStateException() })).should { response ->
            response.shouldBeLeft()
            response.value shouldBe DownloadProcess.Results.Failure
        }
    }

    @Test
    fun `returns retry when receiving a connectivity problem`() = runTest {
        givenAProbe(FakeHttpClient(client, { throw UnresolvedAddressException() })).should { response ->
            response.shouldBeLeft()
            response.value shouldBe DownloadProcess.Results.Retry
        }
    }

    @Test
    fun `returns failure when the response is not successful and not retryable`() = runTest {
        handlers.queue {
            respondOnly(status = HttpStatusCode.NotFound, content = "")
        }

        givenAProbe().should { response ->
            response.shouldBeLeft()
            response.value shouldBe DownloadProcess.Results.Failure
        }
    }

    @Test
    fun `returns retry when the response is retryable`() = runTest {
        handlers.queue {
            respondOnly(status = HttpStatusCode.ServiceUnavailable, content = "")
        }

        givenAProbe().should { response ->
            response.shouldBeLeft()
            response.value shouldBe DownloadProcess.Results.Retry
        }
    }

    @Test
    fun `returns Prime step when the download has never been started`() = runTest {
        handlers.queue { request ->
            respondOnly(
                matcher = { !request.headers.contains(HttpHeaders.Range) },
                status = HttpStatusCode.OK,
                content = CONTENT,
                headers = HEADERS_RESPONSE
            )
        }

        givenAProbe().should { response ->
            response.shouldBeRight()
            response.value.shouldBeInstanceOf<DownloadProcess.Step.Prime>()
        }
    }

    @Test
    fun `returns Prime step when the download was started in the past`() = runTest {
        val downloadFile = File.createTempFile("1234", ".jpg")
        downloadFile.sink().buffer().use { file ->
            file.write("Temp file".toByteArray())
        }

        handlers.queue { request ->
            respondOnly(
                matcher = { request.headers.contains(HttpHeaders.Range) },
                status = HttpStatusCode.PartialContent,
                content = CONTENT,
                headers = HEADERS_RESPONSE
            )
        }

        givenAProbe(download = DownloadsMother.createOngoing(file = downloadFile)).should { response ->
            response.shouldBeRight()
            response.value.shouldBeInstanceOf<DownloadProcess.Step.Prime>()
        }
    }

    private suspend fun givenAProbe(
        client: FakeHttpClient = FakeHttpClient(this.client),
        download: Download = DownloadsMother.createPending()
    ) =
        DownloadProcess.Step.Probe(
            events = {},
            client = client,
            directory = cache,
            log = logger::log,
            download = download
        ).analyze()
}

private const val CONTENT = "This is really a wallpaper, promise!"
private const val HASH = "9cc769f284bba4616668623ca2c22f3e"
private val HEADERS_RESPONSE = headers {
    append(HttpHeaders.ETag, HASH)
    append(HttpHeaders.ContentDisposition, "inline; filename=\"ghost_waves_004.jpg\"")
    append(HttpHeaders.ContentLength, "36")
}
