package com.sefford.artdrian.common.di

import com.sefford.artdrian.Artpplication
import com.sefford.artdrian.downloads.tasks.DownloadTask
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
        CoreModule::class,
        LocalModule::class,
        NetworkModule::class]
)
@Singleton
interface ApplicationComponent {

    fun plus(module: WallpaperDetailModule): WallpaperDetailComponent

    fun inject(application: Artpplication)

    fun inject(activity: WallpaperListActivity)

    fun inject(viewModel: WallpaperListViewModel)

    fun inject(task: DownloadTask)

}
