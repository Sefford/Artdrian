package com.sefford.artdrian.di

import com.sefford.artdrian.common.di.CoreModule
import com.sefford.artdrian.wallpapers.data.datasources.WallpaperLocalDataSourceTest
import com.sefford.artdrian.wallpapers.data.datasources.WallpaperNetworkDataSourceForListsTest
import com.sefford.artdrian.wallpapers.data.datasources.WallpaperNetworkDataSourceForSingleWallpapersTest
import com.sefford.artdrian.wallpapers.ui.detail.di.WallpaperDetailComponent
import com.sefford.artdrian.wallpapers.ui.detail.di.WallpaperDetailModule
import com.sefford.artdrian.wallpapers.ui.list.WallpaperListViewModel
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
