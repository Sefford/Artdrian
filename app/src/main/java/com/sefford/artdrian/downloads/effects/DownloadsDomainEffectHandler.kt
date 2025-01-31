package com.sefford.artdrian.downloads.effects

import com.sefford.artdrian.common.stores.EffectHandler
import com.sefford.artdrian.downloads.domain.model.Download
import com.sefford.artdrian.downloads.domain.model.Downloads
import com.sefford.artdrian.downloads.domain.model.DownloadsResponse
import com.sefford.artdrian.downloads.store.DownloadsEffects
import com.sefford.artdrian.downloads.store.DownloadsEvents
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

class DownloadsDomainEffectHandler(
    private val getAllDownloads: () -> Flow<DownloadsResponse> = { flow {} },
    private val persistDownloads: suspend (Downloads) -> Unit = {},
    private val scope: CoroutineScope = MainScope().plus(Dispatchers.IO)
) : EffectHandler<DownloadsEvents, DownloadsEffects> {

    private val _downloads = Channel<String>(capacity = Int.MAX_VALUE)
    val downloads = _downloads.consumeAsFlow()

    override fun handle(effect: DownloadsEffects, event: (DownloadsEvents) -> Unit) {
        when (effect) {
            DownloadsEffects.LoadAll -> loadAll(event)
            is DownloadsEffects.Register -> scope.launch { persistDownloads(effect.downloads) }
            is DownloadsEffects.Update -> scope.launch { persistDownloads(listOf(effect.download)) }
            is DownloadsEffects.Notify -> scope.launch { effect.downloads.forEach { _downloads.send(it.url) } }
        }
    }

    private fun loadAll(event: (DownloadsEvents) -> Unit) {
        getAllDownloads().onEach { response ->
            response.fold({ _ -> event(DownloadsEvents.OnDownloadsReceived(emptyList())) }) { downloads ->
                event(DownloadsEvents.OnDownloadsReceived(downloads.filterNot { it is Download.Finished }))
            }
        }.launchIn(scope)
    }
}
