package com.sefford.artdrian.wallpapers.di

import android.content.Context
import androidx.room.Room
import com.sefford.artdrian.common.di.Application
import com.sefford.artdrian.wallpapers.data.db.WallpaperDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class WallpapersBoundaryModule {
    @Provides
    @Singleton
    fun provideWallpaperDatabase(@Application context: Context): WallpaperDatabase = Room.databaseBuilder(
        context,
        WallpaperDatabase::class.java, "wallpapers"
    ).fallbackToDestructiveMigration()
        .fallbackToDestructiveMigrationOnDowngrade()
        .fallbackToDestructiveMigrationFrom(1)
        .build()
}
