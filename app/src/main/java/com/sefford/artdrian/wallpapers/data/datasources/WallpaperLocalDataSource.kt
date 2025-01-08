package com.sefford.artdrian.wallpapers.data.datasources

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.sefford.artdrian.common.data.DataError
import com.sefford.artdrian.wallpapers.data.db.WallpaperDao
import com.sefford.artdrian.wallpapers.data.dto.WallpaperDatabaseDto
import com.sefford.artdrian.wallpapers.domain.model.MetadataResponse
import com.sefford.artdrian.wallpapers.domain.model.SingleMetadataResponse
import com.sefford.artdrian.wallpapers.domain.model.Wallpaper
import com.sefford.artdrian.wallpapers.domain.model.WallpaperList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WallpaperLocalDataSource @Inject constructor(private val db: WallpaperDao) : WallpaperDataSource, WallpaperCache {

    override fun getMetadata(): Flow<MetadataResponse> = fetchMetadata(
        query = { db.getAll() },
        transform = { results ->
            if (results.isNotEmpty()) {
                WallpaperList.FromLocal(results.map { entry -> Wallpaper(entry) }).right()
            } else {
                DataError.Local.Empty.left()
            }
        }
    )

    override fun getMetadata(id: String): Flow<SingleMetadataResponse> = fetchMetadata(
        query = { db.get(id) },
        transform = { entry ->
            if (entry != null) {
                Wallpaper(entry).right()
            } else {
                DataError.Local.NotFound(id).left()
            }
        }
    )

    override suspend fun save(wallpapers: List<Wallpaper>) {
        Either.catch { db.add(*wallpapers.map { WallpaperDatabaseDto(it) }.toTypedArray()) }
    }

    override suspend fun save(wallpaper: Wallpaper) = save(listOf(wallpaper))

    override suspend fun delete(id: String) {
        Either.catch { db.delete(id) }
    }

    override suspend fun clear() {
        Either.catch { db.clear() }
    }

    private fun <T, R> fetchMetadata(
        query: () -> Flow<T>,
        transform: suspend (T) -> Either<DataError.Local, R>
    ): Flow<Either<DataError.Local, R>> {
        return Either.catch {
            query()
                .map { result -> transform(result) }
                .catch { emit(DataError.Local.Critical(it).left()) }
        }.fold(
            { flowOf(DataError.Local.Critical(it).left()) },
            { it }
        )
    }
}
