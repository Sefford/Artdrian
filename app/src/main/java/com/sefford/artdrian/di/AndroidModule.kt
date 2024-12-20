package com.sefford.artdrian.di

import android.app.WallpaperManager
import android.content.Context
import com.sefford.artdrian.common.FileManager
import com.sefford.artdrian.common.FileManagerImpl
import com.sefford.artdrian.common.WallpaperAdapter
import com.sefford.artdrian.common.WallpaperAdapterImpl
import dagger.Module
import dagger.Provides
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
class AndroidModule {

    @Provides
    @Singleton
    fun providesWallpaperManager(@Application context: Context): WallpaperManager =  WallpaperManager.getInstance(context)

    @Provides
    @Singleton
    fun provideFileManager(@Application context: Context, client: HttpClient): FileManager = FileManagerImpl(context, client)

    @Provides
    @Singleton
    fun provideWallpaperAdapter(wallpaperManager: WallpaperManager): WallpaperAdapter = WallpaperAdapterImpl(wallpaperManager)
}
