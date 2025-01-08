package com.sefford.artdrian.wallpapers.data.datasources

import com.sefford.artdrian.wallpapers.domain.model.MetadataResponse
import com.sefford.artdrian.wallpapers.domain.model.SingleMetadataResponse
import kotlinx.coroutines.flow.Flow

interface WallpaperDataSource {

    fun getMetadata(): Flow<MetadataResponse>

    fun getMetadata(id: String): Flow<SingleMetadataResponse>

}
