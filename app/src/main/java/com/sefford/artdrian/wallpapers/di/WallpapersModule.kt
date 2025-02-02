package com.sefford.artdrian.wallpapers.di

import android.content.Context
import androidx.room.Room
import com.sefford.artdrian.common.di.Application
import com.sefford.artdrian.common.di.Default
import com.sefford.artdrian.common.di.IO
import com.sefford.artdrian.common.stores.KotlinStore
import com.sefford.artdrian.common.utils.Logger
import com.sefford.artdrian.downloads.data.datasources.DownloadsDataSource
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Singleton

@Module
class WallpapersModule {
    @Provides
    @Singleton
    fun provideWallpaperDao(database: WallpaperDatabase): WallpaperDao = database.dao()

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
    ): WallpaperStore = KotlinStore(WallpaperStateMachine, WallpapersState.Idle.Empty, defaultScope)
        .monitor(logger, "WallpaperStore")
        .also { store ->
            store.effects.onEach { effect -> domainEffectHandler.handle(effect, store::event) }.launchIn(ioScope)
        }
}
