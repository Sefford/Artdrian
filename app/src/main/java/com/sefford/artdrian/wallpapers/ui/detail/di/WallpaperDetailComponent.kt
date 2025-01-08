package com.sefford.artdrian.wallpapers.ui.detail.di

import com.sefford.artdrian.wallpapers.ui.detail.WallpaperDetailViewModel
import dagger.Subcomponent

@Subcomponent(modules = [WallpaperDetailModule::class])
interface WallpaperDetailComponent {

    fun inject(viewModel: WallpaperDetailViewModel)

}
