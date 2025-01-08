package com.sefford.artdrian.datasources

import com.sefford.artdrian.wallpapers.data.datasources.WallpaperNetworkDataSource
import com.sefford.artdrian.wallpapers.data.dto.WallpapersResponse

class FakeWallpaperApi(private val response: () -> WallpapersResponse) : WallpaperNetworkDataSource {

    override suspend fun getAllMetadata(): WallpapersResponse = response()

}
