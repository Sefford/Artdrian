package com.sefford.artdrian.datasources

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.sefford.artdrian.datasources.WallpaperRepository.CachePolicy.*
import com.sefford.artdrian.datasources.WallpaperRepository.RepositoryError.NetworkingError
import com.sefford.artdrian.datasources.WallpaperRepository.RepositoryError.NotFound
import com.sefford.artdrian.model.Metadata
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

class WallpaperRepository @Inject constructor(
    private val api: WallpaperApi,
    private val local: WallpaperMemoryDataSource,
    private val mutex: Mutex = Mutex()
) {

    private suspend fun getAllMetadataFromApi(
        onError: suspend () -> Either<RepositoryError, List<Metadata>> = { NetworkingError().left() }
    ): Either<RepositoryError, List<Metadata>> =
        try {
            val response = api.getAllMetadata()
            mutex.withLock {
                local.saveMetadata(response)
            }
            response.right()
        } catch (x: Exception) {
            onError()
        }

    private suspend fun getAllMetadataFromLocal(
        onError: suspend (RepositoryError) -> Either<RepositoryError, List<Metadata>> = { it.left() }
    ): Either<RepositoryError, List<Metadata>> = local.getAllMetadata().fold({ onError(it) }) { it.right() }

    private suspend fun getWallpaperMetadataFromApi(
        id: String,
        onError: suspend () -> Either<RepositoryError, Metadata> = { NotFound(id).left() }
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

    suspend fun getAllMetadata(cachePolicy: CachePolicy = PRIORITIZE_LOCAL): Either<RepositoryError, List<Metadata>> {
        return when (cachePolicy) {
            PRIORITIZE_LOCAL -> getAllMetadataFromLocal { getAllMetadataFromApi() }
            PRIORITIZE_NETWORK -> getAllMetadataFromApi { getAllMetadataFromLocal() }
            NETWORK_ONLY -> getAllMetadataFromApi()
            OFFLINE -> getAllMetadataFromLocal()
        }
    }

    suspend fun getWallpaperMetadata(
        id: String,
        cachePolicy: CachePolicy = PRIORITIZE_LOCAL
    ): Either<RepositoryError, Metadata> {
        return when (cachePolicy) {
            PRIORITIZE_LOCAL -> getWallpaperMetadataFromLocal(id) { getWallpaperMetadataFromApi(id) }
            PRIORITIZE_NETWORK -> getWallpaperMetadataFromApi(id) { getWallpaperMetadataFromLocal(id) }
            NETWORK_ONLY -> getWallpaperMetadataFromApi(id)
            OFFLINE -> getWallpaperMetadataFromLocal(id)
        }
    }

    sealed class RepositoryError {
        class NotFound(val id: String = "") : RepositoryError()
        class NetworkingError(val status: Int = 0) : RepositoryError()
    }

    enum class CachePolicy {
        PRIORITIZE_LOCAL,
        PRIORITIZE_NETWORK,
        NETWORK_ONLY,
        OFFLINE
    }
}
