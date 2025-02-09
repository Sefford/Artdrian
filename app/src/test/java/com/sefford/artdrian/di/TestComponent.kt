package com.sefford.artdrian.di

import com.sefford.artdrian.common.di.CoreModule
import com.sefford.artdrian.connectivity.di.ConnectivityModule
import com.sefford.artdrian.downloads.data.datasources.DownloadsDataSourceTest
import com.sefford.artdrian.downloads.di.DownloadsModule
import com.sefford.artdrian.downloads.domain.model.DownloadProcessFetchTest
import com.sefford.artdrian.downloads.domain.model.DownloadProcessPrimeTest
import com.sefford.artdrian.downloads.domain.model.DownloadProcessProbeTest
import com.sefford.artdrian.downloads.domain.model.DownloadProcessViabilityTest
import com.sefford.artdrian.notifications.di.NotificationsModule
import com.sefford.artdrian.wallpapers.data.datasources.WallpaperLocalDataSourceTest
import com.sefford.artdrian.wallpapers.data.datasources.WallpaperNetworkDataSourceForListsTest
import com.sefford.artdrian.wallpapers.data.datasources.WallpaperNetworkDataSourceForSingleWallpapersTest
import com.sefford.artdrian.wallpapers.di.WallpapersModule
import com.sefford.artdrian.wallpapers.ui.detail.di.WallpaperDetailComponent
import com.sefford.artdrian.wallpapers.ui.detail.di.WallpaperDetailModule
import com.sefford.artdrian.wallpapers.ui.list.viewmodel.WallpaperListViewModel
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [
        CoreModule::class,
        ConnectivityModule::class,
        DownloadsModule::class,
        DoublesModule::class,
        NotificationsModule::class,
        WallpapersModule::class,
    ]
)
@Singleton
interface TestComponent {

    fun plus(module: WallpaperDetailModule): WallpaperDetailComponent

    fun inject(viewModel: WallpaperListViewModel)

    fun inject(test: WallpaperNetworkDataSourceForListsTest)

    fun inject(test: WallpaperNetworkDataSourceForSingleWallpapersTest)

    fun inject(test: WallpaperLocalDataSourceTest)

    fun inject(test: DownloadsDataSourceTest)

    fun inject(test: DownloadProcessViabilityTest)

    fun inject(test: DownloadProcessProbeTest)

    fun inject(test: DownloadProcessPrimeTest)

    fun inject(test: DownloadProcessFetchTest)
}
