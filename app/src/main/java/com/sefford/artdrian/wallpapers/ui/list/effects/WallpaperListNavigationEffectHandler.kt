package com.sefford.artdrian.wallpapers.ui.list.effects

import androidx.navigation.NavHostController
import com.sefford.artdrian.common.ui.navigation.Routes
import com.sefford.artdrian.common.ui.navigation.navigate
import com.sefford.artdrian.wallpapers.ui.list.viewmodel.WallpaperListEffect

class WallpaperListNavigationEffectHandler(
    private val navigation: NavHostController,
) {
    fun handle(effect: WallpaperListEffect.Navigation) {
        when (effect) {
            is WallpaperListEffect.GoToDetail -> navigation.navigate {
                Routes.WallpaperDetail(effect.id, effect.name)
            }
        }
    }
}
