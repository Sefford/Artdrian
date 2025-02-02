package com.sefford.artdrian.downloads.store

import com.sefford.artdrian.common.stores.StateMachine
import com.sefford.artdrian.downloads.domain.model.Downloads
import com.sefford.artdrian.downloads.domain.model.filterFinished
import com.sefford.artdrian.downloads.store.DownloadsState.Loaded
import com.sefford.artdrian.downloads.store.DownloadsState.Preload

val DownloadsStateMachine: StateMachine<DownloadsEvents, DownloadsState, DownloadsEffects> = { event ->
    fun onPreloadsReceived(event: DownloadsEvents.Register) {
        val received = Preload(event.downloads)
        when(current()) {
            DownloadsState.Empty, is Loaded -> effect(DownloadsEffects.Register(received - current()))
            DownloadsState.Idle, is Preload -> state { it + received }
        }
    }

    fun onDownloadsReceived(downloads: Downloads) {
        val prev = current()
        val received = Loaded(downloads)
        state { received }
        if (prev is Preload) {
            effect(DownloadsEffects.Notify(received.downloads))
            effect(DownloadsEffects.Register((prev - received)))
        } else {
            effect(DownloadsEffects.Notify((received - prev)))
        }
    }

    fun onErrorReceived(event: DownloadsEvents.OnErrorReceived) {
        state { it + event.error }
    }

    when (event) {
        is DownloadsEvents.Register -> onPreloadsReceived(event)
        is DownloadsEvents.OnDownloadsReceived -> onDownloadsReceived(event.downloads)
        is DownloadsEvents.OnErrorReceived -> onErrorReceived(event)
        DownloadsEvents.LoadAll -> effect(DownloadsEffects.LoadAll)
        is DownloadsEvents.Update -> effect(DownloadsEffects.Update(event.download))
        DownloadsEvents.Refresh -> effect(DownloadsEffects.Refresh)
    }
}
