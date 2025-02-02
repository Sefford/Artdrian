package com.sefford.artdrian.wallpapers.store

import com.sefford.artdrian.common.stores.StateMachine
import com.sefford.artdrian.downloads.domain.model.Download

val WallpaperStateMachine: StateMachine<WallpaperEvents, WallpapersState, WallpaperEffects> = { event ->
    fun load() {
        effect(WallpaperEffects.LoadAll)
    }

    fun refresh() {
        state { WallpapersState.Idle.Empty }
        effect(WallpaperEffects.Clear)
        load()
    }

    fun onResponseReceived(event: WallpaperEvents.OnResponseReceived) {
        state { it + event.response }
        if (event.response.network) {
            effect(WallpaperEffects.Persist(event.response.transient))
            effect(WallpaperEffects.PrepareDownloads(
                event.response.wallpapers.flatMap { wallpaper ->
                    listOf(wallpaper.images.mobile, wallpaper.images.desktop)
                }.map { url -> Download.Pending(url) }
                    .toSet()
            )
            )
        }
    }

    when (event) {
        WallpaperEvents.Load -> load()
        WallpaperEvents.Refresh -> refresh()
        is WallpaperEvents.OnErrorReceived -> state { it + event.error }
        is WallpaperEvents.OnResponseReceived -> onResponseReceived(event)
    }
}
