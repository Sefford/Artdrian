package com.sefford.artdrian.notifications

import com.sefford.artdrian.downloads.store.DownloadsEffects
import com.sefford.artdrian.downloads.store.DownloadsStore
import com.sefford.artdrian.notifications.model.Channels
import com.sefford.artdrian.notifications.model.Notifications
import com.sefford.artdrian.notifications.model.NotificationsBridgeState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.scan

fun DownloadsStore.bridgeNotifications(notifications: NotificationCenter, scope: CoroutineScope) =
    effects.bridgeNotifications(
        canNotify = { notifications.canNotifyOnChannel(Channels.DOWNLOAD) },
        notify = notifications::notify,
        clear = { notifications.clear(Notifications.DOWNLOADS_ID) },
        scope = scope
    )

fun Flow<DownloadsEffects>.bridgeNotifications(
    canNotify: () -> Boolean,
    notify: (Notifications.DownloadNotification) -> Unit,
    clear: () -> Unit,
    scope: CoroutineScope
) = filterIsInstance<DownloadsEffects.External>()
    .filter { canNotify() }
    .map { effect ->
        when (effect) {
            is DownloadsEffects.Notify -> NotificationsBridgeState.Notifying(effect.downloads)
            is DownloadsEffects.Update -> NotificationsBridgeState.Notifying(effect.download)
            DownloadsEffects.Refresh -> NotificationsBridgeState.Refreshing
        }
    }
    .scan(NotificationsBridgeState.Idle) { prev: NotificationsBridgeState, next: NotificationsBridgeState ->
        (prev + next)
    }
    .onEach { state ->
        when (state) {
            is NotificationsBridgeState.Notifying -> notify(Notifications.DownloadNotification(state.downloads))
            NotificationsBridgeState.Finished -> clear()
            NotificationsBridgeState.Idle, NotificationsBridgeState.Refreshing -> {}
        }
    }
    .launchIn(scope)
