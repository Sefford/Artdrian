package com.sefford.artdrian.wallpapers.ui.detail.effects

import com.sefford.artdrian.wallpapers.ui.detail.viewmodel.WallpaperDetailsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

fun WallpaperDetailsViewModel.bridgeEffectHandler(handle: (WallpaperDetailsEffect) -> Unit, scope: CoroutineScope) =
    effects.onEach(handle).launchIn(scope)
