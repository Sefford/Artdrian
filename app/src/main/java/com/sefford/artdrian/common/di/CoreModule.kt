package com.sefford.artdrian.common.di

import androidx.work.WorkManager
import com.sefford.artdrian.common.data.network.DelegatedHttpClient
import com.sefford.artdrian.common.stores.monitor
import com.sefford.artdrian.common.utils.DefaultLogger
import com.sefford.artdrian.common.utils.Logger
import com.sefford.artdrian.connectivity.Connectivity
import com.sefford.artdrian.connectivity.ConnectivityStore
import com.sefford.artdrian.connectivity.ConnectivitySubscription
import com.sefford.artdrian.downloads.data.datasources.DownloadsDataSource
import com.sefford.artdrian.downloads.db.DownloadsDao
import com.sefford.artdrian.downloads.db.DownloadsDatabase
import com.sefford.artdrian.downloads.effects.DownloadsDomainEffectHandler
import com.sefford.artdrian.downloads.store.Downloader
import com.sefford.artdrian.downloads.store.DownloadsState
import com.sefford.artdrian.downloads.store.DownloadsStateMachine
import com.sefford.artdrian.downloads.store.DownloadsStore
import com.sefford.artdrian.downloads.store.bridgeDownloader
import com.sefford.artdrian.downloads.store.bridgeToDownload
import com.sefford.artdrian.wallpapers.data.datasources.WallpaperCache
import com.sefford.artdrian.wallpapers.data.datasources.WallpaperLocalDataSource
import com.sefford.artdrian.wallpapers.data.datasources.WallpaperNetworkDataSource
import com.sefford.artdrian.wallpapers.data.datasources.WallpaperRepository
import com.sefford.artdrian.wallpapers.data.db.WallpaperDao
import com.sefford.artdrian.wallpapers.data.db.WallpaperDatabase
import com.sefford.artdrian.wallpapers.domain.model.MetadataResponse
import com.sefford.artdrian.wallpapers.domain.model.SingleMetadataResponse
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
import io.ktor.client.plugins.cache.storage.CacheStorage
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.json.Json
import javax.inject.Singleton
import com.sefford.artdrian.common.data.network.HttpClient as DelegatedClient

@Module
class CoreModule {
    private val TIME_OUT = 10_000L

    @Provides
    @Singleton
    fun provideDeserialization(): Json = Json {
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
    fun provideWallpaperDao(database: WallpaperDatabase): WallpaperDao = database.dao()

    @Provides
    @Singleton
    fun provideDownloadsDao(database: DownloadsDatabase): DownloadsDao = database.dao()

    @Provides
    @Singleton
    fun provideRepository(
        local: WallpaperLocalDataSource,
        network: WallpaperNetworkDataSource,
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
        cache: WallpaperCache,
        downloads: DownloadsDataSource
    ): WallpaperDomainEffectHandler {
        val getAllMetadata: () -> Flow<MetadataResponse> = repository::getMetadata
        val getSingleMetadata: (String) -> Flow<SingleMetadataResponse> = repository::getMetadata
        return WallpaperDomainEffectHandler(getAllMetadata, getSingleMetadata, cache::save, downloads::save, cache::clear)
    }

    @Provides
    @Singleton
    fun provideWallpaperStore(
        domainEffectHandler: WallpaperDomainEffectHandler,
        logger: Logger,
        @IO ioScope: CoroutineScope,
        @Default defaultScope: CoroutineScope
    ): WallpaperStore = WallpaperStore(WallpaperStateMachine, WallpapersState.Idle.Empty, defaultScope).also { store ->
        store.effects.onEach { effect -> domainEffectHandler.handle(effect, store::event) }.launchIn(ioScope)
    }.monitor(logger, "WallpaperStore")

    @Provides
    @Singleton
    fun provideConnectivityStore(
        initial: Connectivity,
        subscription: ConnectivitySubscription,
    ): ConnectivityStore = ConnectivityStore(initial).also { subscription.start(it) }

    @Provides
    @Singleton
    fun provideDownloadsDomainEffectHandler(
        cache: DownloadsDataSource
    ): DownloadsDomainEffectHandler = DownloadsDomainEffectHandler(cache::getAll, cache::save)

    @Provides
    @Singleton
    fun provideDownloadsStore(
        domainEffectHandler: DownloadsDomainEffectHandler,
        wallpaperStore: WallpaperStore,
        logger: Logger,
        @IO ioScope: CoroutineScope,
        @Default defaultScope: CoroutineScope,
    ): DownloadsStore = DownloadsStore(DownloadsStateMachine, DownloadsState.Empty, defaultScope).also { store ->
        store.effects.onEach { effect -> domainEffectHandler.handle(effect, store::event) }.launchIn(ioScope)
        wallpaperStore.state.bridgeToDownload(store::event, defaultScope)
    }.monitor(logger, "DownloadsStore")

    @Provides
    @Singleton
    fun provideDownloader(
        workManager: WorkManager,
        downloads: DownloadsStore,
        connectivity: ConnectivityStore,
        @Default scope: CoroutineScope,
    ) = Downloader(workManager, connectivity, scope).also { downloader ->
        downloads.state.bridgeDownloader(downloader::queue, scope)
    }
}

