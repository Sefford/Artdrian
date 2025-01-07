package com.sefford.artdrian.datasources

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import com.sefford.artdrian.Endpoints
import com.sefford.artdrian.data.DataError
import com.sefford.artdrian.data.dto.WallpaperResponse
import com.sefford.artdrian.data.dto.WallpapersResponse
import com.sefford.artdrian.data.network.HttpCache
import com.sefford.artdrian.data.network.HttpClient
import com.sefford.artdrian.language.flatMapLeft
import com.sefford.artdrian.model.MetadataResponse
import com.sefford.artdrian.model.SingleMetadataResponse
import com.sefford.artdrian.model.Wallpaper
import com.sefford.artdrian.model.WallpaperList.FromNetwork
import io.ktor.client.call.body
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.nio.channels.UnresolvedAddressException
import javax.inject.Inject

class WallpaperNetworkDataSource @Inject constructor(
    private val client: HttpClient,
    private val cache: HttpCache,
) : WallpaperDataSource {

    private val allWallpapersUrl = "${Endpoints.ENDPOINT}index.json"
    private val wallpaperUrl = "${Endpoints.ENDPOINT}wallpapers/%s.json"

    override fun getMetadata(): Flow<MetadataResponse> =
        flow {
            emit(
                request(
                    { client.get(allWallpapersUrl) },
                    { cache.get<WallpapersResponse>(allWallpapersUrl) }
                ) { response ->
                    FromNetwork(response.wallpapers.map { Wallpaper(it) })
                })
        }

    override fun getMetadata(id: String): Flow<SingleMetadataResponse> =
        flow {
            val url = wallpaperUrl.format(id)
            emit(request(
                { client.get(url) },
                { cache.get<WallpaperResponse>(url) }
            ) { response ->
                Wallpaper(response.wallpaper)
            })
        }

    private suspend inline fun <reified R, T> request(
        request: () -> HttpResponse,
        fallback: () -> R?,
        transform: (R) -> T
    ): Either<DataError, T> = Either.catch {
        request()
    }
        .toDataError()
        .flatMap { response ->
            when {
                response.status.isSuccess() -> Either.catch { response.retrieve<R>() }.toDataError()
                else -> DataError.Network.Invalid(response.status.value).left()
            }
        }
        .flatMapLeft { error ->
            fallback()?.right() ?: error.left()
        }.flatMap { response ->
            Either.catch { transform(response) }.toDataError()
        }

    private suspend inline fun <reified T> HttpResponse.retrieve(): T = body<T>()

    private fun <R> Either<Throwable, R>.toDataError() = mapLeft { error ->
        when (error) {
            is UnresolvedAddressException -> DataError.Network.NoConnection
            is ConnectTimeoutException -> DataError.Network.ConnectTimeout
            is SocketTimeoutException -> DataError.Network.SocketTimeout
            else -> DataError.Local.Critical(error)
        }
    }
}
