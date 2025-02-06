package com.sefford.artdrian.wallpapers.ui.list.di

import androidx.navigation.NavHostController
import com.sefford.artdrian.wallpapers.ui.list.effects.WallpaperListNavigationEffectHandler
import dagger.Module
import dagger.Provides
import javax.inject.Inject

@Module
class WallpaperListModule(private val navigation: NavHostController) {

    @Provides
    fun provideNavigationEffectHandler(): WallpaperListNavigationEffectHandler = WallpaperListNavigationEffectHandler(navigation)

}
