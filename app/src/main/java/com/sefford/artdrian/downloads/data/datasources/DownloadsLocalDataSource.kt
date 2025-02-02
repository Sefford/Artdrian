package com.sefford.artdrian.downloads.data.datasources

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.sefford.artdrian.common.data.DataError
import com.sefford.artdrian.common.utils.Logger
import com.sefford.artdrian.downloads.db.DownloadsDao
import com.sefford.artdrian.downloads.domain.model.Download
import com.sefford.artdrian.downloads.domain.model.Downloads
import com.sefford.artdrian.downloads.domain.model.DownloadsResponse
import com.sefford.artdrian.downloads.domain.model.filterFinished
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DownloadsDataSource constructor(
    private val db: DownloadsDao,
    private val log: (String, String) -> Unit = { _, _ -> }
) {

    @Inject
    constructor(
        db: DownloadsDao,
        logger: Logger,
    ) : this(db, logger::log)

    fun getAll(): Flow<DownloadsResponse> =
        Either.catch {
            db.getAll().map { response ->
                if (response.isNotEmpty()) {
                    response.map { Download(it) }.toSet().right()
                } else {
                    DataError.Local.Empty.left<DataError>()
                }
            }.catch { emit(DataError.Local.Critical(it).left<DataError>()) }
        }.fold({
            flow { emit(DataError.Local.Critical(it).left()) }
        }) { it }

    suspend fun save(downloads: Downloads) {
        Either.catch { db.add(*downloads.filterFinished().map { it.toDto() }.toTypedArray()) }
            .mapLeft { log("DownloadsDataSource", "Error: $it") }
    }

    suspend fun get(id: String) = db.get(id)

    suspend fun delete(id: String) = db.delete(id)
}
