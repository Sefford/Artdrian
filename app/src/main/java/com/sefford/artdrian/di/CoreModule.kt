package com.sefford.artdrian.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.sefford.artdrian.data.datasources.WallpaperApi
import com.sefford.artdrian.data.datasources.WallpaperMemoryDataSource
import com.sefford.artdrian.data.datasources.WallpaperRepository
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class CoreModule(val endpoint: String) {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient()

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder()
        .create()

    @Provides
    fun provideRetrofit(client: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(endpoint)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideLocalWallpaperCache(): WallpaperMemoryDataSource = WallpaperMemoryDataSource()

    @Provides
    @Singleton
    fun provideWallpaperRepository(api: WallpaperApi, local: WallpaperMemoryDataSource): WallpaperRepository =
        WallpaperRepository(api, local)
}
