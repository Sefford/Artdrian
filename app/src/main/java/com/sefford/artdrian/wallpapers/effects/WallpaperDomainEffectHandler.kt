package com.sefford.artdrian.wallpapers.effects

import com.sefford.artdrian.model.Metadata
import com.sefford.artdrian.model.MetadataResponse
import com.sefford.artdrian.model.SingleMetadataResponse
import com.sefford.artdrian.model.WallpaperList
import com.sefford.artdrian.stores.EffectHandler
import com.sefford.artdrian.wallpapers.store.WallpaperEffects
import com.sefford.artdrian.wallpapers.store.WallpaperEvents
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

class WallpaperDomainEffectHandler(
    private val getAllMetadata: () -> Flow<MetadataResponse> = { flow {} },
    private val getSingleMetadata: (String) -> Flow<SingleMetadataResponse> = { flow {} },
    private val persistMetadata: suspend (List<Metadata>) -> Unit = {},
    private val clear: suspend () -> Unit = {},
    scope: CoroutineScope = MainScope().plus(Dispatchers.IO)
) : EffectHandler<WallpaperEvents, WallpaperEffects>(scope) {

    override suspend fun handleEffect(effect: WallpaperEffects, event: (WallpaperEvents) -> Unit) {
        when (effect) {
            WallpaperEffects.LoadAll -> loadAll(event)
            is WallpaperEffects.Load -> load(effect.id, event)
            is WallpaperEffects.Persist -> persist(effect.metadata)
            WallpaperEffects.Clear -> clear()
        }
    }

    private fun loadAll(event: (WallpaperEvents) -> Unit) {
        getAllMetadata().onEach { response ->
            response.fold({ error -> event(WallpaperEvents.OnErrorReceived(error)) }) { wallpapers ->
                event(WallpaperEvents.OnResponseReceived(wallpapers))
            }
        }.launchIn(scope)
    }

    private fun load(id: String, event: (WallpaperEvents) -> Unit) {
        getSingleMetadata(id).onEach { response ->
            response.fold({ error -> event(WallpaperEvents.OnErrorReceived(error)) }) { wallpaper ->
                event(WallpaperEvents.OnResponseReceived(WallpaperList(wallpaper)))
            }
        }.launchIn(scope)
    }

    private fun persist(metadata: List<Metadata>) {
        scope.launch { persistMetadata(metadata) }
    }
}