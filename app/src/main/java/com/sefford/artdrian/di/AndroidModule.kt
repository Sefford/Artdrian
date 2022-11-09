package com.sefford.artdrian.di

import android.app.WallpaperManager
import android.content.Context
import androidx.core.content.ContextCompat
import com.sefford.artdrian.common.FileManager
import com.sefford.artdrian.common.FileManagerImpl
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
class AndroidModule {

    @Provides
    @Singleton
    fun providesWallpaperManager(@Application context: Context): WallpaperManager =  WallpaperManager.getInstance(context)

    @Provides
    @Singleton
    fun provideFileManager(@Application context: Context, client: OkHttpClient): FileManager = FileManagerImpl(context, client)
}
