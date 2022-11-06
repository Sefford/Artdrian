package com.sefford.artdrian.di

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.sefford.artdrian.Artpplication
import com.sefford.artdrian.datasources.WallpaperApi
import com.sefford.artdrian.datasources.WallpaperMemoryDataSource
import com.sefford.artdrian.datasources.WallpaperRepository
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import com.sefford.decompose.di.Application as ApplicationContext

@Module
class ApplicationModule(val application: Artpplication) {

    @Provides
    fun provideApplication(): Application = application

    @Provides
    fun provideArtpplication(): Artpplication = application

    @Provides
    @ApplicationContext
    fun provideApplicationContext(): Context = application

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
            .baseUrl("https://adrianmg-go-server.fly.dev/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideWallpaperApi(retrofit: Retrofit): WallpaperApi = retrofit.create(WallpaperApi::class.java)

    @Provides
    @Singleton
    fun provideLocalWallpaperCache(): WallpaperMemoryDataSource = WallpaperMemoryDataSource()

    @Provides
    @Singleton
    fun provideWallpaperRepository(api: WallpaperApi, local: WallpaperMemoryDataSource): WallpaperRepository =
        WallpaperRepository(api, local)

}
