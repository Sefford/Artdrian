package com.sefford.artdrian.datasources

import com.sefford.artdrian.model.MetadataResponse
import com.sefford.artdrian.model.SingleMetadataResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.merge

class WallpaperRepository(
    private val getAllMetadataLocal: () -> Flow<MetadataResponse>,
    private val getAllMetadataNetwork: () -> Flow<MetadataResponse>,
    private val getMetadataLocal: (String) -> Flow<SingleMetadataResponse>,
    private val getMetadataNetwork: (String) -> Flow<SingleMetadataResponse>,
): WallpaperDataSource {

    override fun getMetadata(): Flow<MetadataResponse> = merge(getAllMetadataLocal(), getAllMetadataNetwork())

    override fun getMetadata(id: String): Flow<SingleMetadataResponse> = merge(getMetadataLocal(id), getMetadataNetwork(id))
}
