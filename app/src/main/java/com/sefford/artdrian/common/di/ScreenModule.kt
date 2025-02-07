package com.sefford.artdrian.common.di

import androidx.navigation.NavHostController
import com.sefford.artdrian.wallpapers.ui.list.effects.WallpaperListNavigationEffectHandler
import dagger.Module
import dagger.Provides
import javax.inject.Inject

@Module
class ScreenModule(private val navigation: NavHostController) {

    @Provides
    fun providesNavigation(): NavHostController = navigation

}
