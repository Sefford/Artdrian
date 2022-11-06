package com.sefford.artdrian.datasources

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.sefford.artdrian.datasources.WallpaperRepository.CachePolicy.*
import com.sefford.artdrian.datasources.WallpaperRepository.RepositoryError.NetworkingError
import com.sefford.artdrian.datasources.WallpaperRepository.RepositoryError.NotFound
import com.sefford.artdrian.model.Metadata
import javax.inject.Inject

class WallpaperRepository @Inject constructor(
    private val api: WallpaperApi,
    private val local: WallpaperMemoryDataSource
) {

    suspend fun getAllMetadata(cachePolicy: CachePolicy = PRIORIZE_LOCAL): Either<RepositoryError, List<Metadata>> {
        return when (cachePolicy) {
            PRIORIZE_LOCAL -> {
                when (val metadata = local.getAllMetadata()) {
                    is Either.Left -> try {
                        val response = api.getAllMetadata()
                        local.saveMetadata(response)
                        response.right()
                    } catch (x: Exception) {
                        NetworkingError().left()
                    }
                    is Either.Right -> metadata
                }
            }
            ALWAYS_NETWORK -> try {
                api.getAllMetadata().right()
            } catch (x: Exception) {
                NetworkingError().left()
            }
            LOCAL_ONLY -> when (val metadata = local.getAllMetadata()) {
                is Either.Left -> NotFound().left()
                is Either.Right -> metadata
            }
        }
    }

    suspend fun getWallpaperMetadata(
        id: String,
        cachePolicy: CachePolicy = PRIORIZE_LOCAL
    ): Either<RepositoryError, Metadata> {
        return when (cachePolicy) {
            PRIORIZE_LOCAL -> {
                when (val allMetadata = local.getWallpaperMetadata(id)) {
                    is Either.Left -> getAllMetadata(ALWAYS_NETWORK).fold({ error -> error.left() }) { allMetadata ->
                                    allMetadata.find { metadata -> metadata.id == id }
                                    ?.right() ?: NotFound(id).left()
                    }
                    is Either.Right -> allMetadata
                }
            }
            ALWAYS_NETWORK -> try {
                api.getAllMetadata()
                    .find { metadata -> metadata.id == id }
                    ?.right() ?: NotFound(id).left()
            } catch (x: Exception) {
                NetworkingError().left()
            }
            LOCAL_ONLY -> local.getWallpaperMetadata(id)
        }
    }

    sealed class RepositoryError {
        class NotFound(val id: String = "") : RepositoryError()
        class NetworkingError(val status: Int = 0) : RepositoryError()
    }

    enum class CachePolicy {
        PRIORIZE_LOCAL,
        ALWAYS_NETWORK,
        LOCAL_ONLY
    }
}
