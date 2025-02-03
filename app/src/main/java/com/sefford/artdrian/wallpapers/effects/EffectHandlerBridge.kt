package com.sefford.artdrian.wallpapers.effects

import com.sefford.artdrian.wallpapers.store.WallpaperStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

fun WallpaperStore.bridgeEffectHandler(
    effectHandler: WallpaperDomainEffectHandler,
    scope: CoroutineScope
) {
    effects.onEach { effect -> effectHandler.handle(effect, ::event) }
        .launchIn(scope)
}
