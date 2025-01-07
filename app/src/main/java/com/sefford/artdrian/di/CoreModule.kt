package com.sefford.artdrian.di

import com.sefford.artdrian.data.db.WallpaperDao
import com.sefford.artdrian.data.db.WallpaperDatabase
import com.sefford.artdrian.data.network.DelegatedHttpClient
import com.sefford.artdrian.datasources.WallpaperCache
import com.sefford.artdrian.datasources.WallpaperLocalDataSource
import com.sefford.artdrian.datasources.WallpaperNetworkDataSource
import com.sefford.artdrian.datasources.WallpaperRepository
import com.sefford.artdrian.model.MetadataResponse
import com.sefford.artdrian.model.SingleMetadataResponse
import com.sefford.artdrian.utils.DefaultLogger
import com.sefford.artdrian.utils.Logger
import com.sefford.artdrian.utils.forceCache
import com.sefford.artdrian.utils.isFromCache
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
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.cache.storage.CacheStorage
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
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
import javax.inject.Singleton
import com.sefford.artdrian.data.network.HttpClient as DelegatedClient

@Module
class CoreModule {
    private val TIME_OUT = 10_000L

    @Provides
    @Singleton
    protected fun provideDeserialization(): Json = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    fun provideHttpClient(
        deserialization: Json,
        engine: HttpClientEngineFactory<*> = CIO,
        storage: CacheStorage,
        logger: Logger = DefaultLogger()
    ): HttpClient {
        return HttpClient(engine) {
            install(ContentNegotiation) {
                json(json = deserialization, contentType = ContentType.parse("text/x-component"))
            }

            install(Logging) {
                this.logger = logger
                level = LogLevel.ALL
            }

            install(ResponseObserver) {
                onResponse { response ->
                    logger.debug("TAG_HTTP_STATUS_LOGGER", "${response.status.value}")
                }
            }

            install(DefaultRequest) {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
            }

            install(HttpCache) {
                publicStorage(storage)
            }
        }
    }

    @Provides
    @Singleton
    fun provideDelegatedClient(client: HttpClient): DelegatedClient = DelegatedHttpClient(client)

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
