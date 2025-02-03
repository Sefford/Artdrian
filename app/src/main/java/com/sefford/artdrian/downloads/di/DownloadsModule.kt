package com.sefford.artdrian.downloads.di

import android.content.Context
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
import com.sefford.artdrian.downloads.effects.bridgeEffectHandler
import com.sefford.artdrian.downloads.store.Downloader
import com.sefford.artdrian.downloads.store.DownloadsEvents
import com.sefford.artdrian.downloads.store.DownloadsState
import com.sefford.artdrian.downloads.store.DownloadsStateMachine
import com.sefford.artdrian.downloads.store.DownloadsStore
import com.sefford.artdrian.downloads.store.bridgeDownloader
import com.sefford.artdrian.notifications.NotificationCenter
import com.sefford.artdrian.notifications.bridgeNotifications
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
class DownloadsModule {

    @Provides
    @Singleton
    fun provideWorkManager(@Application context: Context): WorkManager = WorkManager.getInstance(context)

    @Provides
    @Singleton
    fun provideDownloadsDao(database: DownloadsDatabase): DownloadsDao = database.dao()

    @Provides
    @Singleton
    fun provideDownloadsDomainEffectHandler(
        cache: DownloadsDataSource,
        @Default defaultScope: CoroutineScope,
    ): DownloadsDomainEffectHandler = DownloadsDomainEffectHandler(cache, defaultScope)

    @Provides
    @Singleton
    fun provideDownloader(
        workManager: WorkManager,
        connectivity: ConnectivityStore,
        notifications: NotificationCenter,
        logger: Logger,
        @Default scope: CoroutineScope,
    ) = Downloader(workManager, connectivity, notifications::clearAll, logger::log, scope)

    @Provides
    @Singleton
    fun provideDownloadsStore(
        effectHandler: DownloadsDomainEffectHandler,
        downloader: Downloader,
        notifications: NotificationCenter,
        logger: Logger,
        @IO ioScope: CoroutineScope,
        @Default defaultScope: CoroutineScope,
    ): DownloadsStore = KotlinStore(DownloadsStateMachine, DownloadsState.Idle, defaultScope)
        .monitor(logger, "DownloadsStore")
        .also { store ->
            store.bridgeEffectHandler(effectHandler, ioScope)
            store.bridgeNotifications(notifications, defaultScope)
            store.bridgeDownloader(downloader::queue, defaultScope)
            store.event(DownloadsEvents.LoadAll)
        }
}
