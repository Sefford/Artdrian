package com.sefford.artdrian.downloads.store

import com.sefford.artdrian.common.stores.StateMachine
import com.sefford.artdrian.common.stores.StoreEventProcessor
import com.sefford.artdrian.downloads.domain.model.Downloads

val DownloadsStateMachine: StateMachine<DownloadsEvents, DownloadsState, DownloadsEffects> = { event ->
    fun onPreloadsReceived(event: DownloadsEvents.Register) {
        state { it + DownloadsState.Preload(event.downloads) }
        persist()
    }

    fun onDownloadsReceived(downloads: Downloads) {
        state { it + downloads }
        persist()
    }

    fun onErrorReceived(event: DownloadsEvents.OnErrorReceived) {
        state { it + event.error }
        persist()
    }

    when (event) {
        is DownloadsEvents.Register -> onPreloadsReceived(event)
        is DownloadsEvents.OnDownloadsReceived -> onDownloadsReceived(event.downloads)
        is DownloadsEvents.OnErrorReceived -> onErrorReceived(event)
        DownloadsEvents.LoadAll -> effect(DownloadsEffects.LoadAll)
    }
}

private fun StoreEventProcessor<DownloadsEvents, DownloadsState, DownloadsEffects>.persist() {
    val current = current()
    if (current is DownloadsState.Loaded) {
        effect(DownloadsEffects.Register(current.pending))
    }
}
