package com.sefford.artdrian.di

import com.sefford.artdrian.wallpaperdetail.di.WallpaperDetailComponent
import com.sefford.artdrian.wallpaperdetail.di.WallpaperDetailModule
import com.sefford.artdrian.wallpaperdetail.ui.WallpaperDetailViewModel
import com.sefford.artdrian.wallpaperlist.ui.WallpaperListViewModel
import dagger.Component
import javax.inject.Singleton

@Component(modules = [
    ApiModule::class,
    CoreModule::class,
    DoublesModule::class])
@Singleton
interface TestComponent {

    fun plus(module: WallpaperDetailModule): WallpaperDetailComponent

    fun inject(viewModel: WallpaperListViewModel)

}
