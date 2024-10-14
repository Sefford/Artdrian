package com.sefford.artdrian.datasources

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.sefford.artdrian.datasources.WallpaperRepository.RepositoryError
import com.sefford.artdrian.datasources.WallpaperRepository.RepositoryError.NotFound
import com.sefford.artdrian.data.dto.MetadataDto
import com.sefford.artdrian.model.Metadata

class WallpaperMemoryDataSource(
    private val list: MutableList<Metadata> = mutableListOf(),
    private val indexed: MutableMap<String, Metadata> = mutableMapOf()
) : WallpaperDataSource {

    override suspend fun saveMetadata(metadataList: List<Metadata>) {
        this.list.clear()
        this.list.addAll(metadataList)
        this.indexed.clear()
        this.indexed.putAll(metadataList.associateBy { metadata -> metadata.id })
    }

    override suspend fun getWallpaperMetadata(id: String) : Either<RepositoryError, Metadata> =
        indexed[id]?.right() ?: NotFound(id).left()

    override suspend fun getAllMetadata(): Either<RepositoryError, List<Metadata>> {
        return if (list.isNotEmpty()) list.right() else NotFound().left()
    }

}
