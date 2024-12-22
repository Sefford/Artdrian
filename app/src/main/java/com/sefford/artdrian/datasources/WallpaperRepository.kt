package com.sefford.artdrian.datasources

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.sefford.artdrian.data.RepositoryError
import com.sefford.artdrian.model.Metadata
import javax.inject.Inject

class WallpaperRepository @Inject constructor(
    private val api: WallpaperApi,
    private val local: WallpaperMemoryDataSource,
) {

    suspend fun getAllMetadata(cachePolicy: CachePolicy = CachePolicy.PRIORITIZE_LOCAL): Either<RepositoryError,
        List<Metadata>> {
        return when (cachePolicy) {
            CachePolicy.PRIORITIZE_LOCAL -> getAllMetadataFromLocal { getAllMetadataFromApi() }
            CachePolicy.PRIORITIZE_NETWORK -> getAllMetadataFromApi { getAllMetadataFromLocal() }
            CachePolicy.NETWORK_ONLY -> getAllMetadataFromApi()
            CachePolicy.OFFLINE -> getAllMetadataFromLocal()
        }
    }

    suspend fun getWallpaperMetadata(
        id: String,
        cachePolicy: CachePolicy = CachePolicy.PRIORITIZE_LOCAL
    ): Either<RepositoryError, Metadata> {
        return when (cachePolicy) {
            CachePolicy.PRIORITIZE_LOCAL -> getWallpaperMetadataFromLocal(id) { getWallpaperMetadataFromApi(id) }
            CachePolicy.PRIORITIZE_NETWORK -> getWallpaperMetadataFromApi(id) { getWallpaperMetadataFromLocal(id) }
            CachePolicy.NETWORK_ONLY -> getWallpaperMetadataFromApi(id)
            CachePolicy.OFFLINE -> getWallpaperMetadataFromLocal(id)
        }
    }

    private suspend fun getAllMetadataFromApi(
        onError: suspend () -> Either<RepositoryError, List<Metadata>> = { RepositoryError.NetworkingError().left() }
    ): Either<RepositoryError, List<Metadata>> =
        try {
            api.getAllMetadata().wallpapers
                .map { Metadata(it) }
                .also { response -> local.saveMetadata(response) }.right()
        } catch (x: Exception) {
            onError()
        }

    private suspend fun getAllMetadataFromLocal(
        onError: suspend (RepositoryError) -> Either<RepositoryError, List<Metadata>> = { it.left() }
    ): Either<RepositoryError, List<Metadata>> =
        local.getAllMetadata().fold({ onError(it) }) { it.right() }

    private suspend fun getWallpaperMetadataFromApi(
        id: String,
        onError: suspend () -> Either<RepositoryError, Metadata> = { RepositoryError.NotFound(id).left() }
    ): Either<RepositoryError, Metadata> =
        getAllMetadataFromApi()
            .fold({ it.left() }) { allMetadata ->
                allMetadata.find { metadata -> metadata.id == id }?.right() ?: onError()
            }

    private suspend fun getWallpaperMetadataFromLocal(
        id: String,
        onError: suspend (RepositoryError) -> Either<RepositoryError, Metadata> = { it.left() }
    ): Either<RepositoryError, Metadata> =
        local.getWallpaperMetadata(id).fold({ onError(it) }) { it.right() }
}
