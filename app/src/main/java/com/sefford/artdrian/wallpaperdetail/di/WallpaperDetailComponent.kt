package com.sefford.artdrian.wallpaperdetail.di

import com.sefford.artdrian.wallpaperdetail.ui.WallpaperDetailViewModel
import dagger.Subcomponent

@Subcomponent(modules = [WallpaperDetailModule::class])
interface WallpaperDetailComponent {

    fun inject(viewModel: WallpaperDetailViewModel)

}
