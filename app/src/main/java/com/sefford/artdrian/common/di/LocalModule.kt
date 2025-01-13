package com.sefford.artdrian.common.di

import android.content.Context
import androidx.room.Room
import com.sefford.artdrian.downloads.db.DownloadsDatabase
import com.sefford.artdrian.wallpapers.data.db.WallpaperDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LocalModule {
    @Provides
    @Singleton
    fun provideWallpaperDatabase(@Application context: Context): WallpaperDatabase = Room.databaseBuilder(
        context,
        WallpaperDatabase::class.java, "wallpapers"
    ).fallbackToDestructiveMigration()
        .fallbackToDestructiveMigrationOnDowngrade()
        .build()

    @Provides
    @Singleton
    fun provideDownloadsDatabase(@Application context: Context): DownloadsDatabase = Room.databaseBuilder(
        context,
        DownloadsDatabase::class.java, "downloads"
    ).fallbackToDestructiveMigration()
        .fallbackToDestructiveMigrationOnDowngrade()
        .build()
}
