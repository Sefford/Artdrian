package com.sefford.artdrian.wallpapers.store

import com.sefford.artdrian.stores.Store
import kotlinx.coroutines.CoroutineScope

class WallpaperStore(
    initial: WallpapersState,
    scope: CoroutineScope
) : Store<WallpaperEvents, WallpapersState, WallpaperEffects>(initial, scope) {

    override fun handleEvent(event: WallpaperEvents) {
        when(event) {
            WallpaperEvents.Load -> load()
            WallpaperEvents.Refresh -> refresh()
            is WallpaperEvents.OnErrorReceived -> state { it + event.error }
            is WallpaperEvents.OnResponseReceived -> onResponseReceived(event)
        }
    }

    private fun load() {
        effect(WallpaperEffects.LoadAll)
    }

    private fun refresh() {
        state { WallpapersState.Idle }
        effect(WallpaperEffects.Clear)
        load()
    }

    private fun onResponseReceived(event: WallpaperEvents.OnResponseReceived) {
        state { it + event.response }
        effect(WallpaperEffects.Persist(state.value.transient))
    }
}
