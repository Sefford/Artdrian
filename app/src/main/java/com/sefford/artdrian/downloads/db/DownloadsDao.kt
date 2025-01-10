package com.sefford.artdrian.downloads.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.sefford.artdrian.downloads.data.dto.DownloadDto
import kotlinx.coroutines.flow.Flow

@Dao
interface DownloadsDao {
    @Upsert
    fun add(vararg downloads: DownloadDto)

    @Query("SELECT * FROM downloads")
    fun getAll(): Flow<List<DownloadDto>>

    @Query("SELECT * FROM downloads WHERE id == :id")
    fun get(id: String): Flow<DownloadDto?>

    @Query("DELETE FROM downloads WHERE id = :id")
    fun delete(id: String)

    @Query("DELETE FROM downloads")
    fun clear()

}
