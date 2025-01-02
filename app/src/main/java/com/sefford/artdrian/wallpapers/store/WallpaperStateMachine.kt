package com.sefford.artdrian.wallpapers.store

import com.sefford.artdrian.stores.StateMachine

val WallpaperStateMachine: StateMachine<WallpaperEvents, WallpapersState, WallpaperEffects> = { event ->
    fun load() {
        effect(WallpaperEffects.LoadAll)
    }

    fun refresh() {
        state { WallpapersState.Idle }
        effect(WallpaperEffects.Clear)
        load()
    }

    fun onResponseReceived(event: WallpaperEvents.OnResponseReceived) {
        state { it + event.response }
        effect(WallpaperEffects.Persist(current().transient))
    }

    when(event) {
        WallpaperEvents.Load -> load()
        WallpaperEvents.Refresh -> refresh()
        is WallpaperEvents.OnErrorReceived -> state { it + event.error }
        is WallpaperEvents.OnResponseReceived -> onResponseReceived(event)
    }
}
