package com.sefford.artdrian.wallpapers.ui.list.di

import com.sefford.artdrian.common.di.Main
import com.sefford.artdrian.common.di.ScreenModule
import com.sefford.artdrian.wallpapers.ui.list.viewmodel.WallpaperListViewModel
import com.sefford.artdrian.wallpapers.ui.list.effects.WallpaperListNavigationEffectHandler
import dagger.Subcomponent
import kotlinx.coroutines.CoroutineScope

@Subcomponent(modules = [ScreenModule::class])
interface WallpaperListComponent {

    fun viewModel(): WallpaperListViewModel.Provider

}
