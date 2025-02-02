package com.sefford.artdrian.downloads.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sefford.artdrian.downloads.data.dto.DownloadDto

@Database(entities = [DownloadDto::class], version = 2)
abstract class DownloadsDatabase : RoomDatabase() {
    abstract fun dao(): DownloadsDao
}
