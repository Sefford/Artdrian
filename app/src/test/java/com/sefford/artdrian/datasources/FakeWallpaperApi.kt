package com.sefford.artdrian.datasources

import com.sefford.artdrian.data.datasources.WallpaperApi
import com.sefford.artdrian.model.Metadata

class FakeWallpaperApi(private val response: () -> List<Metadata>) : WallpaperApi {

    override suspend fun getAllMetadata(): List<Metadata> = response()

}
