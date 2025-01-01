package com.sefford.artdrian.data.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.sefford.artdrian.data.dto.WallpaperDatabaseDto
import kotlinx.coroutines.flow.Flow

@Dao
interface WallpaperDao {
    @Upsert
    fun add(vararg wallpapers: WallpaperDatabaseDto)

    @Query("SELECT * FROM wallpapers")
    fun getAll(): Flow<List<WallpaperDatabaseDto>>

    @Query("SELECT * FROM wallpapers WHERE id == :id")
    fun get(id: String): Flow<WallpaperDatabaseDto>

    @Query("DELETE FROM wallpapers WHERE id = :id")
    fun delete(id: String)

    @Query("DELETE FROM wallpapers")
    fun clear()
}
