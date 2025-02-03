package com.sefford.artdrian.common.di

import android.app.WallpaperManager
import android.content.Context
import com.sefford.artdrian.common.FileManager
import com.sefford.artdrian.common.FileManagerImpl
import com.sefford.artdrian.common.Permissions
import com.sefford.artdrian.common.WallpaperAdapter
import com.sefford.artdrian.common.WallpaperAdapterImpl
import com.sefford.artdrian.common.utils.DefaultLogger
import com.sefford.artdrian.common.utils.Logger
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
    @Downloads
    fun provideExternalCacheDirForDownloads(@Application context: Context) = File(context.externalCacheDir, "downloads")

    @Provides
    fun provideResources(@Application context: Context) = context.resources

    @Provides
    @Singleton
    fun providePackageManager(@Application context: Context) = context.packageManager

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
    fun providePermissions(@Application context: Context) = Permissions(context)

    @Provides
    @Singleton
    fun provideLogger(logger: DefaultLogger): Logger = logger

}
