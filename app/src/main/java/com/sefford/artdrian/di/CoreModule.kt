package com.sefford.artdrian.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.sefford.artdrian.data.dto.WallpaperResponse
import com.sefford.artdrian.data.dto.deserializers.WallpaperResponseDeserializer
import com.sefford.artdrian.datasources.WallpaperApi
import com.sefford.artdrian.datasources.WallpaperMemoryDataSource
import com.sefford.artdrian.datasources.WallpaperRepository
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class CoreModule(val endpoint: String) {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient().newBuilder().addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        ).build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder()
        .registerTypeAdapter(WallpaperResponse::class.java, WallpaperResponseDeserializer())
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
    fun provideWallpaperRepository(
        api: WallpaperApi,
        local: WallpaperMemoryDataSource
    ): WallpaperRepository =
        WallpaperRepository(api, local)
}
