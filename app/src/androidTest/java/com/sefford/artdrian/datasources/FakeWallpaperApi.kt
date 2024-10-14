package com.sefford.artdrian.datasources

import com.sefford.artdrian.data.dto.WallpaperResponse

class FakeWallpaperApi(private val response: () -> WallpaperResponse) : WallpaperApi {

    override suspend fun getAllMetadata(): WallpaperResponse = response()

}
