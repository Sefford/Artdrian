package com.sefford.artdrian.di

import android.app.WallpaperManager
import android.content.Context
import com.sefford.artdrian.common.FileManager
import com.sefford.artdrian.common.FileManagerImpl
import com.sefford.artdrian.common.WallpaperAdapter
import com.sefford.artdrian.common.WallpaperAdapterImpl
import com.sefford.artdrian.utils.DefaultLogger
import com.sefford.artdrian.utils.Logger
import dagger.Module
import dagger.Provides
import io.ktor.client.HttpClient
import java.io.File
import javax.inject.Singleton

@Module
class AndroidModule {

    @Provides
    @Singleton
    @NetworkCache
    fun provideExternalCacheDirForHttpCache(@Application context: Context) = File(context.externalCacheDir, "http_cache")

    @Provides
    @Singleton
    fun providesWallpaperManager(@Application context: Context): WallpaperManager = WallpaperManager.getInstance(context)

    @Provides
    @Singleton
    fun provideFileManager(@Application context: Context, client: HttpClient): FileManager = FileManagerImpl(context, client)

    @Provides
    @Singleton
    fun provideWallpaperAdapter(wallpaperManager: WallpaperManager): WallpaperAdapter = WallpaperAdapterImpl(wallpaperManager)

    @Provides
    @Singleton
    fun provideLogger(logger: DefaultLogger): Logger = logger
}
