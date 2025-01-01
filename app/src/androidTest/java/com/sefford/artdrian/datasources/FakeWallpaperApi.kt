package com.sefford.artdrian.datasources

import com.sefford.artdrian.data.dto.WallpapersResponse

class FakeWallpaperApi(private val response: () -> WallpapersResponse) : WallpaperNetworkDataSource {

    override suspend fun getAllMetadata(): WallpapersResponse = response()

}
