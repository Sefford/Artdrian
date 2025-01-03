package com.sefford.artdrian.di

import com.sefford.artdrian.Artpplication
import com.sefford.artdrian.wallpaperdetail.di.WallpaperDetailComponent
import com.sefford.artdrian.wallpaperdetail.di.WallpaperDetailModule
import com.sefford.artdrian.wallpaperlist.ui.WallpaperListActivity
import com.sefford.artdrian.wallpaperlist.ui.WallpaperListViewModel
import dagger.Component
import javax.inject.Singleton

@Component(modules = [
    ApplicationModule::class,
    AndroidModule::class,
    ConcurrencyModule::class,
    CoreModule::class])
@Singleton
interface ApplicationComponent {

    fun plus(module: WallpaperDetailModule): WallpaperDetailComponent

    fun inject(application: Artpplication)

    fun inject(activity: WallpaperListActivity)

    fun inject(viewModel: WallpaperListViewModel)

}
