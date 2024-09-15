package com.sefford.artdrian.data.datasources

import com.sefford.artdrian.data.dto.MetadataDto
import retrofit2.http.GET
import com.sefford.artdrian.model.Metadata

interface WallpaperApi {

    @GET("/")
    suspend fun getAllMetadata(): List<MetadataDto>
}
