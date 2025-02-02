package com.sefford.artdrian.common.di

import com.sefford.artdrian.Artpplication
import com.sefford.artdrian.connectivity.di.ConnectivityModule
import com.sefford.artdrian.downloads.di.DownloadBoundariesModule
import com.sefford.artdrian.downloads.di.DownloadsModule
import com.sefford.artdrian.downloads.tasks.DownloadTask
import com.sefford.artdrian.notifications.di.NotificationsModule
import com.sefford.artdrian.wallpapers.di.WallpapersBoundaryModule
import com.sefford.artdrian.wallpapers.di.WallpapersModule
import com.sefford.artdrian.wallpapers.ui.detail.di.WallpaperDetailComponent
import com.sefford.artdrian.wallpapers.ui.detail.di.WallpaperDetailModule
import com.sefford.artdrian.wallpapers.ui.list.WallpaperListActivity
import com.sefford.artdrian.wallpapers.ui.list.WallpaperListViewModel
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [
        ApplicationModule::class,
        AndroidModule::class,
        ConcurrencyModule::class,
        ConnectivityModule::class,
        CoreModule::class,
        DownloadBoundariesModule::class,
        DownloadsModule::class,
        NotificationsModule::class,
        NetworkModule::class,
        WallpapersBoundaryModule::class,
        WallpapersModule::class,
    ]
)
@Singleton
interface ApplicationComponent {

    fun plus(module: WallpaperDetailModule): WallpaperDetailComponent

    fun inject(application: Artpplication)

    fun inject(activity: WallpaperListActivity)

    fun inject(viewModel: WallpaperListViewModel)

    fun inject(task: DownloadTask)

}
