package com.sefford.artdrian.datasources

import com.sefford.artdrian.data.datasources.WallpaperApi
import com.sefford.artdrian.data.dto.MetadataDto

class FakeWallpaperApi(private val response: () -> List<MetadataDto>) : WallpaperApi {

    override suspend fun getAllMetadata(): List<MetadataDto> = response()

}
