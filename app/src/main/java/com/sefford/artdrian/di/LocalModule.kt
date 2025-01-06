package com.sefford.artdrian.di

import android.content.Context
import androidx.room.Room
import com.sefford.artdrian.data.db.WallpaperDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LocalModule {
    @Provides
    @Singleton
    fun provideDatabase(@Application context: Context): WallpaperDatabase = Room.databaseBuilder(
        context,
        WallpaperDatabase::class.java, "wallpapers"
    ).fallbackToDestructiveMigration()
        .fallbackToDestructiveMigrationOnDowngrade()
        .build()
}
