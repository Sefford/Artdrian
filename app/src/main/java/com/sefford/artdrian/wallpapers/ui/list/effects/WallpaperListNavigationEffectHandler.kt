package com.sefford.artdrian.wallpapers.ui.list.effects

import androidx.navigation.NavHostController
import com.sefford.artdrian.common.ui.navigation.Routes
import com.sefford.artdrian.common.ui.navigation.navigate
import javax.inject.Inject

class WallpaperListNavigationEffectHandler @Inject constructor(
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
