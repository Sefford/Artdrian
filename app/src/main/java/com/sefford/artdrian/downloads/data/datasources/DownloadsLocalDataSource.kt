package com.sefford.artdrian.downloads.data.datasources

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.sefford.artdrian.common.data.DataError
import com.sefford.artdrian.downloads.db.DownloadsDao
import com.sefford.artdrian.downloads.domain.model.Download
import com.sefford.artdrian.downloads.domain.model.Downloads
import com.sefford.artdrian.downloads.domain.model.DownloadsResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DownloadsDataSource @Inject constructor(private val db: DownloadsDao) {

    suspend fun save(downloads: Downloads) {
        Either.catch { db.add(*downloads.map { it.toDto() }.toTypedArray()) }
    }

    fun getAll(): Flow<DownloadsResponse> =
        Either.catch {
            db.getAll().map { response ->
                if (response.isNotEmpty()) {
                    Downloads(response.map { Download(it) }.filterNot { it.finished }).right()
                } else {
                    DataError.Local.Empty.left<DataError>()
                }
            }.catch { emit(DataError.Local.Critical(it).left<DataError>()) }
        }.fold({
            flow { emit(DataError.Local.Critical(it).left()) }
        }) { it }
}
