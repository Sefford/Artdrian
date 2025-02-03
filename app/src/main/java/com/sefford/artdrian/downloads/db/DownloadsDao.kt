package com.sefford.artdrian.downloads.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.sefford.artdrian.downloads.data.dto.DownloadDto
import kotlinx.coroutines.flow.Flow

@Dao
interface DownloadsDao {
    @Upsert
    suspend fun add(vararg downloads: DownloadDto)

    @Query("SELECT * FROM downloads")
    fun getAll(): Flow<List<DownloadDto>>

    @Query("SELECT * FROM downloads WHERE url == :url")
    suspend fun get(url: String): DownloadDto?

    @Query("DELETE FROM downloads WHERE url = :id")
    suspend fun delete(id: String)

    @Query("DELETE FROM downloads")
    suspend fun clear()

}
