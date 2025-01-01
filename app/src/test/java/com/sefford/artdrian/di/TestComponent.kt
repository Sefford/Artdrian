package com.sefford.artdrian.di

import com.sefford.artdrian.datasources.WallpaperNetworkDataSourceTest
import com.sefford.artdrian.test.InjectableTest
import com.sefford.artdrian.wallpaperdetail.di.WallpaperDetailComponent
import com.sefford.artdrian.wallpaperdetail.di.WallpaperDetailModule
import com.sefford.artdrian.wallpaperlist.ui.WallpaperListViewModel
import dagger.Component
import javax.inject.Singleton

@Component(modules = [
    CoreModule::class,
    DoublesModule::class])
@Singleton
interface TestComponent {

    fun plus(module: WallpaperDetailModule): WallpaperDetailComponent

    fun inject(viewModel: WallpaperListViewModel)

    fun inject(test: InjectableTest)

    fun inject(test: WallpaperNetworkDataSourceTest)

}
