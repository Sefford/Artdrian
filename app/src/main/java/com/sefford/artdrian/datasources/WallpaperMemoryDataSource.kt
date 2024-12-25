package com.sefford.artdrian.datasources

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.sefford.artdrian.data.RepositoryError
import com.sefford.artdrian.di.Memory
import com.sefford.artdrian.model.Metadata
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WallpaperMemoryDataSource @Inject constructor(
    private val list: MutableList<Metadata> = mutableListOf(),
    private val indexed: MutableMap<String, Metadata> = mutableMapOf(),
    @Memory private val scope: CoroutineScope
) : WallpaperDataSource {

    override suspend fun saveMetadata(metadata: List<Metadata>) {
        scope.launch {
            list.clear()
            list.addAll(metadata)
            indexed.clear()
            indexed.putAll(metadata.associateBy { metadata -> metadata.id })
        }
    }

    override suspend fun getWallpaperMetadata(id: String) : Either<RepositoryError, Metadata> =
        indexed[id]?.right() ?: RepositoryError.NotFound(id).left()

    override suspend fun getAllMetadata(): Either<RepositoryError, List<Metadata>> {
        return if (list.isNotEmpty()) list.right() else RepositoryError.NotFound().left()
    }
}
