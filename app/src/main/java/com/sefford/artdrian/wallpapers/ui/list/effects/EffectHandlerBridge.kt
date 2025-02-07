package com.sefford.artdrian.wallpapers.ui.list.effects

import com.sefford.artdrian.wallpapers.ui.list.viewmodel.WallpaperListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

fun WallpaperListViewModel.bridgeEffectHandler(handle: (WallpaperListEffect.Navigation) -> Unit, scope: CoroutineScope) =
    effects.filterIsInstance<WallpaperListEffect.Navigation>().onEach(handle).launchIn(scope)
