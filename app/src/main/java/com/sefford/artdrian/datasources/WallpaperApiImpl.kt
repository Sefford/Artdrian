package com.sefford.artdrian.datasources

import com.sefford.artdrian.Endpoints
import com.sefford.artdrian.data.dto.MetadataDto
import com.sefford.artdrian.data.dto.WallpaperResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject

class WallpaperApiImpl @Inject constructor(private val httpClient: HttpClient) : WallpaperApi {

    override suspend fun getAllMetadata(): WallpaperResponse = httpClient.get(METADATA).body<WallpaperResponse>()

    companion object {
        const val METADATA = Endpoints.ENDPOINT + "index.json"
    }
}
