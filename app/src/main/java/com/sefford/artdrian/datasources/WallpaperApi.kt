package com.sefford.artdrian.datasources

import com.sefford.artdrian.data.dto.deserializers.WallpaperResponse
import retrofit2.http.GET

interface WallpaperApi {

    @GET("index.json")
    suspend fun getAllMetadata(): WallpaperResponse
}
