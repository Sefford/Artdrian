package com.sefford.artdrian.downloads.store

import com.sefford.artdrian.downloads.domain.model.Download
import com.sefford.artdrian.wallpapers.store.WallpapersState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

fun StateFlow<WallpapersState>.bridgeToDownload(event: (DownloadsEvents) -> Unit, scope: CoroutineScope) =
    filterIsInstance<WallpapersState.Loaded>()
        .map {
            it.wallpapers.flatMap { wallpaper ->
                listOf(Download.Pending(wallpaper.images.mobile))
                listOf(Download.Pending(wallpaper.images.desktop))
            }
        }.onEach { event(DownloadsEvents.Register(it)) }
        .launchIn(scope)
