package com.sefford.artdrian.di

import android.util.Log
import com.sefford.artdrian.datasources.WallpaperApi
import com.sefford.artdrian.datasources.WallpaperApiImpl
import com.sefford.artdrian.datasources.WallpaperMemoryDataSource
import com.sefford.artdrian.datasources.WallpaperRepository
import dagger.Module
import dagger.Provides
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
class CoreModule {
    private val TIME_OUT = 10_000

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(CIO) {
            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    }, contentType = ContentType.parse("text/x-component")
                )
            }


            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.v("Logger Ktor =>", message)
                    }
                }
                level = LogLevel.ALL
            }

            install(ResponseObserver) {
                onResponse { response ->
                    Log.d("TAG_HTTP_STATUS_LOGGER", "${response.status.value}")
                }
            }

            install(DefaultRequest) {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
            }

            engine {
                requestTimeout = TIME_OUT.toLong()
            }
        }
    }

    @Provides
    @Singleton
    fun provideApiService(httpClient: HttpClient): WallpaperApi {
        return WallpaperApiImpl(httpClient)
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
