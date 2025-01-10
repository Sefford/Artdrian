package com.sefford.artdrian.wallpapers.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sefford.artdrian.wallpapers.data.dto.WallpaperDatabaseDto

@Database(entities = [WallpaperDatabaseDto::class], version = 1)
abstract class WallpaperDatabase : RoomDatabase() {
    abstract fun dao(): WallpaperDao
}
