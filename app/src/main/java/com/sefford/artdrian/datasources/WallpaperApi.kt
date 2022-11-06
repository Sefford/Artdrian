package com.sefford.artdrian.datasources

import retrofit2.http.GET
import com.sefford.artdrian.model.Metadata

interface WallpaperApi {

    @GET("/")
    suspend fun getAllMetadata(): List<Metadata>
}
