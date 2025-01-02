package com.sefford.artdrian.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.sefford.artdrian.data.db.WallpaperDao
import com.sefford.artdrian.data.db.WallpaperDatabase
import com.sefford.artdrian.datasources.WallpaperCache
import com.sefford.artdrian.datasources.WallpaperLocalDataSource
import com.sefford.artdrian.datasources.WallpaperNetworkDataSource
import com.sefford.artdrian.datasources.WallpaperRepository
import com.sefford.artdrian.model.MetadataResponse
import com.sefford.artdrian.model.SingleMetadataResponse
import com.sefford.artdrian.stores.KotlinStore
import com.sefford.artdrian.wallpapers.effects.WallpaperDomainEffectHandler
import com.sefford.artdrian.wallpapers.store.WallpaperStateMachine
import com.sefford.artdrian.wallpapers.store.WallpaperStore
import com.sefford.artdrian.wallpapers.store.WallpapersState
import dagger.Module
import dagger.Provides
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.cache.storage.FileStorage
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.plus
import kotlinx.serialization.json.Json
import java.io.File
import javax.inject.Singleton

@Module
class CoreModule {
    private val TIME_OUT = 10_000L

    @Provides
    @Singleton
    fun provideEngine(): HttpClientEngineFactory<*> = CIO

    @Provides
    @Singleton
    fun provideHttpClient(@NetworkCache httpCacheDir: File, engine: HttpClientEngineFactory<*> = CIO
    ): HttpClient {
        return HttpClient(engine) {
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

            install(HttpCache) {
                // Configure FileStorage for persistent caching
                publicStorage(FileStorage(httpCacheDir))
            }
        }
    }

    @Provides
    @Singleton
    fun provideDatabase(@Application context: Context): WallpaperDatabase = Room.databaseBuilder(
        context,
        WallpaperDatabase::class.java, "wallpapers"
    ).build()

    @Provides
    @Singleton
    fun provideDao(database: WallpaperDatabase): WallpaperDao = database.dao()

    @Provides
    @Singleton
    fun provideRepository(
        local: WallpaperLocalDataSource,
        network: WallpaperNetworkDataSource
    ): WallpaperRepository {
        val getAllMetadataLocal: () -> Flow<MetadataResponse> = local::getMetadata
        val getAllMetadataNetwork: () -> Flow<MetadataResponse> = network::getMetadata
        val getSingleMetadataLocal: (String) -> Flow<SingleMetadataResponse> = local::getMetadata
        val getSingleMetadataNetwork: (String) -> Flow<SingleMetadataResponse> = network::getMetadata
        return WallpaperRepository(getAllMetadataLocal, getAllMetadataNetwork, getSingleMetadataLocal, getSingleMetadataNetwork)
    }

    @Provides
    @Singleton
    fun provideWallpaperCache(dataSource: WallpaperLocalDataSource): WallpaperCache = dataSource

    @Provides
    @Singleton
    fun provideWallpaperDomainEffectHandler(
        repository: WallpaperRepository,
        cache: WallpaperCache
    ): WallpaperDomainEffectHandler {
        val getAllMetadata: () -> Flow<MetadataResponse> = repository::getMetadata
        val getSingleMetadata: (String) -> Flow<SingleMetadataResponse> = repository::getMetadata
        return WallpaperDomainEffectHandler(getAllMetadata, getSingleMetadata, cache::save, cache::clear)
    }

    @Provides
    @Singleton
    fun provideWallpaperStore(domainEffectHandler: WallpaperDomainEffectHandler): WallpaperStore {
        val store = WallpaperStore(WallpaperStateMachine, WallpapersState.Idle)
        store.effects.onEach { effect -> domainEffectHandler.handle(effect, store::event) }
            .launchIn(MainScope().plus(Dispatchers.IO))
        return store
    }
}
