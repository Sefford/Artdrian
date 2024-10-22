package com.sefford.artdrian.datasources

import com.sefford.artdrian.data.dto.WallpaperResponse

interface WallpaperApi {

    suspend fun getAllMetadata(): WallpaperResponse
}
