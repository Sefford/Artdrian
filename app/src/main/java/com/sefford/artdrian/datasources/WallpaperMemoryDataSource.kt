package com.sefford.artdrian.datasources

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.sefford.artdrian.datasources.WallpaperRepository.RepositoryError
import com.sefford.artdrian.datasources.WallpaperRepository.RepositoryError.NotFound
import com.sefford.artdrian.data.dto.MetadataDto

class WallpaperMemoryDataSource(
    private val list: MutableList<MetadataDto> = mutableListOf(),
    private val indexed: MutableMap<String, MetadataDto> = mutableMapOf()
) : WallpaperDataSource {

    override suspend fun saveMetadata(metadataDtoList: List<MetadataDto>) {
        this.list.clear()
        this.list.addAll(metadataDtoList)
        this.indexed.clear()
        this.indexed.putAll(metadataDtoList.associateBy { metadata -> metadata.id })
    }

    override suspend fun getWallpaperMetadata(id: String) : Either<RepositoryError, MetadataDto> =
        indexed[id]?.right() ?: NotFound(id).left()

    override suspend fun getAllMetadata(): Either<RepositoryError, List<MetadataDto>> {
        return if (list.isNotEmpty()) list.right() else NotFound().left()
    }

}
