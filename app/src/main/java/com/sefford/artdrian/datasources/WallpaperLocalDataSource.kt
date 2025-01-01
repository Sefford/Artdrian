package com.sefford.artdrian.datasources

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.sefford.artdrian.data.DataError
import com.sefford.artdrian.data.db.WallpaperDao
import com.sefford.artdrian.data.dto.WallpaperDatabaseDto
import com.sefford.artdrian.model.Metadata
import com.sefford.artdrian.model.MetadataResponse
import com.sefford.artdrian.model.SingleMetadataResponse
import com.sefford.artdrian.model.Source
import com.sefford.artdrian.model.Wallpaper
import com.sefford.artdrian.model.WallpaperList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WallpaperLocalDataSource @Inject constructor(private val db: WallpaperDao) : WallpaperDataSource, WallpaperCache {

    override fun getMetadata(): Flow<MetadataResponse> = db.getAll()
        .map { results ->
            if (results.isNotEmpty()) {
                WallpaperList(results.map { entry -> Metadata(entry) }, Source.LOCAL).right()
            } else {
                DataError.Local.NotFound("").left()
            }
        }
        .catch { DataError.Local.Critical(it).left() }

    override fun getMetadata(id: String): Flow<SingleMetadataResponse> = db.get(id)
        .map { entry -> Wallpaper(Metadata(entry), Source.LOCAL).right() }
        .catch { DataError.Local.Critical(it) }

    override suspend fun save(metadata: List<Metadata>) {
        Either.catch { db.add(*metadata.map { WallpaperDatabaseDto(it) }.toTypedArray()) }
    }

    override suspend fun save(metadata: Metadata) = save(listOf(metadata))

    override suspend fun delete(id: String) {
        Either.catch { db.delete(id) }
    }

    override suspend fun clear() {
        Either.catch { db.clear() }
    }
}
