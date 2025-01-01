package com.sefford.artdrian.datasources

import com.sefford.artdrian.model.MetadataResponse
import com.sefford.artdrian.model.SingleMetadataResponse
import kotlinx.coroutines.flow.Flow

interface WallpaperDataSource {

    fun getMetadata(): Flow<MetadataResponse>

    fun getMetadata(id: String): Flow<SingleMetadataResponse>

}
