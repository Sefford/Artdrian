package com.sefford.artdrian.common.di

import android.app.WallpaperManager
import android.content.Context
import android.net.ConnectivityManager
import com.sefford.artdrian.common.FileManager
import com.sefford.artdrian.common.FileManagerImpl
import com.sefford.artdrian.common.WallpaperAdapter
import com.sefford.artdrian.common.WallpaperAdapterImpl
import com.sefford.artdrian.common.utils.DefaultLogger
import com.sefford.artdrian.common.utils.Logger
import com.sefford.artdrian.connectivity.Connectivity
import com.sefford.artdrian.connectivity.ConnectivitySubscription
import com.sefford.artdrian.connectivity.DefaultConnectivitySubscription
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

    @Provides
    @Singleton
    fun provideConnectivityManager(@Application context: Context): ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @Provides
    @Singleton
    fun provideConnectivitySubscription(manager: ConnectivityManager): ConnectivitySubscription =
        DefaultConnectivitySubscription(manager)

    @Provides
    fun provideDefaultConnectivity(manager: ConnectivityManager): Connectivity =
        manager.getNetworkCapabilities(manager.activeNetwork)?.let { Connectivity(it) } ?: Connectivity.Undetermined
}
