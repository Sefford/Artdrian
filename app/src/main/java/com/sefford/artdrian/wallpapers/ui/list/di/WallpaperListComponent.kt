package com.sefford.artdrian.wallpapers.ui.list.di

import com.sefford.artdrian.wallpapers.ui.list.WallpaperListViewModel
import dagger.Subcomponent

@Subcomponent(modules = [WallpaperListModule::class])
interface WallpaperListComponent {

    fun viewModel(): WallpaperListViewModel.Provider

}
