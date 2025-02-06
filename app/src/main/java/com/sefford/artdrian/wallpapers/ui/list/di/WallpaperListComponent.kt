package com.sefford.artdrian.wallpapers.ui.list.di

import com.sefford.artdrian.common.di.Main
import com.sefford.artdrian.wallpapers.ui.list.WallpaperListViewModel
import com.sefford.artdrian.wallpapers.ui.list.effects.WallpaperListNavigationEffectHandler
import dagger.Subcomponent
import kotlinx.coroutines.CoroutineScope

@Subcomponent(modules = [WallpaperListModule::class])
interface WallpaperListComponent {

    fun viewModel(): WallpaperListViewModel.Provider

    fun effectHandler(): WallpaperListNavigationEffectHandler

    @Main
    fun mainScope(): CoroutineScope

}
