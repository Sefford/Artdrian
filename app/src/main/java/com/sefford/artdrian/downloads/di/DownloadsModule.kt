package com.sefford.artdrian.downloads.di

import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import com.sefford.artdrian.common.di.Application
import com.sefford.artdrian.common.di.Default
import com.sefford.artdrian.common.di.IO
import com.sefford.artdrian.common.stores.KotlinStore
import com.sefford.artdrian.common.utils.Logger
import com.sefford.artdrian.connectivity.ConnectivityStore
import com.sefford.artdrian.downloads.data.datasources.DownloadsDataSource
import com.sefford.artdrian.downloads.db.DownloadsDao
import com.sefford.artdrian.downloads.db.DownloadsDatabase
import com.sefford.artdrian.downloads.effects.DownloadsDomainEffectHandler
import com.sefford.artdrian.downloads.store.Downloader
import com.sefford.artdrian.downloads.store.DownloadsEvents
import com.sefford.artdrian.downloads.store.DownloadsState
import com.sefford.artdrian.downloads.store.DownloadsStateMachine
import com.sefford.artdrian.downloads.store.DownloadsStore
import com.sefford.artdrian.downloads.store.bridgeDownloader
import com.sefford.artdrian.downloads.store.bridgeToDownload
import com.sefford.artdrian.notifications.NotificationCenter
import com.sefford.artdrian.wallpapers.store.WallpaperStore
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Singleton

@Module
class DownloadsModule {

    @Provides
    @Singleton
    fun provideWorkManager(@Application context: Context): WorkManager = WorkManager.getInstance(context)

    @Provides
    @Singleton
    fun provideDownloadsDatabase(@Application context: Context): DownloadsDatabase = Room.databaseBuilder(
        context,
        DownloadsDatabase::class.java, "downloads"
    ).fallbackToDestructiveMigration()
        .fallbackToDestructiveMigrationOnDowngrade()
        .fallbackToDestructiveMigrationFrom(1)
        .build()

    @Provides
    @Singleton
    fun provideDownloadsDao(database: DownloadsDatabase): DownloadsDao = database.dao()

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
    ): DownloadsStore = KotlinStore(DownloadsStateMachine, DownloadsState.Idle, defaultScope)
        .monitor(logger, "DownloadsStore")
        .also { store ->
            store.effects.onEach { effect -> domainEffectHandler.handle(effect, store::event) }.launchIn(ioScope)
            wallpaperStore.state.bridgeToDownload(store::event, defaultScope)
            store.event(DownloadsEvents.LoadAll)
        }

    @Provides
    @Singleton
    fun provideDownloader(
        workManager: WorkManager,
        downloads: DownloadsDomainEffectHandler,
        connectivity: ConnectivityStore,
        notifications: NotificationCenter,
        logger: Logger,
        @Default scope: CoroutineScope,
    ) = Downloader(workManager, connectivity, notifications::clearDownloadNotification, logger::log, scope).also { downloader ->
        downloads.downloads.bridgeDownloader(downloader::queue, scope)
    }
}
