package com.sefford.artdrian.datasources

import arrow.core.Either
import arrow.core.flatten
import arrow.core.left
import arrow.core.right
import com.sefford.artdrian.Endpoints
import com.sefford.artdrian.data.DataError
import com.sefford.artdrian.data.dto.WallpaperResponse
import com.sefford.artdrian.data.dto.WallpapersResponse
import com.sefford.artdrian.model.MetadataResponse
import com.sefford.artdrian.model.SingleMetadataResponse
import com.sefford.artdrian.model.Wallpaper
import com.sefford.artdrian.model.WallpaperList.FromNetwork
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WallpaperNetworkDataSource @Inject constructor(private val httpClient: HttpClient) : WallpaperDataSource {

    private val metadataUrl = "${Endpoints.ENDPOINT}index.json"
    private val wallpaperUrl = "${Endpoints.ENDPOINT}wallpapers/%s.json"

    override fun getMetadata(): Flow<MetadataResponse> =
        flow {
            emit(
                request({ httpClient.get(metadataUrl) }) { response ->
                    FromNetwork(response.body<WallpapersResponse>().wallpapers.map { Wallpaper(it) })
                })
        }

    override fun getMetadata(id: String): Flow<SingleMetadataResponse> =
        flow {
            emit(request({ httpClient.get(wallpaperUrl.format(id)) }) { response ->
                Wallpaper(response.body<WallpaperResponse>().wallpaper)
            })
        }

    private inline fun <R> request(
        request: () -> HttpResponse,
        transform: (HttpResponse) -> R
    ): Either<DataError, R> = Either.catch {
        val response = request()
        if (response.status.isSuccess()) {
            transform(response).right()
        } else {
            DataError.Network.Invalid(response.status.value).left()
        }
    }.mapLeft { DataError.Network.Critical(it) }
        .flatten()

}
