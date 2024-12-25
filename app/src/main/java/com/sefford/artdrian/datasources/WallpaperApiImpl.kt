package com.sefford.artdrian.datasources

import com.sefford.artdrian.Endpoints
import com.sefford.artdrian.data.dto.WallpaperResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject

class WallpaperApiImpl @Inject constructor(private val httpClient: HttpClient) : WallpaperApi {

    private val metadataUrl = Endpoints.ENDPOINT + "index.json"

    override suspend fun getAllMetadata(): WallpaperResponse = httpClient.get(metadataUrl).body<WallpaperResponse>()

}
