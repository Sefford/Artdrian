package com.sefford.artdrian.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sefford.artdrian.data.dto.WallpaperDatabaseDto

@Database(entities = [WallpaperDatabaseDto::class], version = 1)
abstract class WallpaperDatabase : RoomDatabase() {
    abstract fun dao(): WallpaperDao
}
