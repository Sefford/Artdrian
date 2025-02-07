package com.sefford.artdrian.wallpapers.ui.detail.di

import com.sefford.artdrian.common.di.Default
import com.sefford.artdrian.common.di.ScreenModule
import com.sefford.artdrian.wallpapers.ui.detail.effects.WallpaperDetailsEffectHandler
import com.sefford.artdrian.wallpapers.ui.detail.viewmodel.WallpaperDetailsViewModel
import dagger.Subcomponent
import kotlinx.coroutines.CoroutineScope

@Subcomponent(modules = [ScreenModule::class, WallpaperDetailModule::class])
interface WallpaperDetailComponent {

    fun viewModel(): WallpaperDetailsViewModel.Provider

    fun effectHandler(): WallpaperDetailsEffectHandler

    @Default
    fun defaultScope(): CoroutineScope
}
