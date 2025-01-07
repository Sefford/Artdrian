package com.sefford.artdrian.di

import com.sefford.artdrian.datasources.WallpaperLocalDataSourceTest
import com.sefford.artdrian.datasources.WallpaperNetworkDataSourceForListsTest
import com.sefford.artdrian.datasources.WallpaperNetworkDataSourceForSingleWallpapersTest
import com.sefford.artdrian.wallpaperdetail.di.WallpaperDetailComponent
import com.sefford.artdrian.wallpaperdetail.di.WallpaperDetailModule
import com.sefford.artdrian.wallpaperlist.ui.WallpaperListViewModel
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [
        CoreModule::class,
        DoublesModule::class]
)
@Singleton
interface TestComponent {

    fun plus(module: WallpaperDetailModule): WallpaperDetailComponent

    fun inject(viewModel: WallpaperListViewModel)

    fun inject(test: WallpaperNetworkDataSourceForListsTest)

    fun inject(test: WallpaperNetworkDataSourceForSingleWallpapersTest)

    fun inject(test: WallpaperLocalDataSourceTest)

}
