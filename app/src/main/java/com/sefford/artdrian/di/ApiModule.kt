package com.sefford.artdrian.di

import com.sefford.artdrian.datasources.WallpaperApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class ApiModule {

    @Provides
    @Singleton
    fun provideWallpaperApi(retrofit: Retrofit): WallpaperApi = retrofit.create(WallpaperApi::class.java)

}
