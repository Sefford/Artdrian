package com.sefford.artdrian.downloads.di

import android.content.Context
import androidx.room.Room
import com.sefford.artdrian.common.di.Application
import com.sefford.artdrian.downloads.db.DownloadsDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DownloadBoundariesModule {

    @Provides
    @Singleton
    fun provideDownloadsDatabase(@Application context: Context): DownloadsDatabase = Room.databaseBuilder(
        context,
        DownloadsDatabase::class.java, "downloads"
    ).fallbackToDestructiveMigration()
        .fallbackToDestructiveMigrationOnDowngrade()
        .fallbackToDestructiveMigrationFrom(1)
        .build()
}
