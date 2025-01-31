package com.sefford.artdrian.notifications

import com.sefford.artdrian.downloads.domain.model.Download
import com.sefford.artdrian.downloads.domain.model.Downloads
import com.sefford.artdrian.downloads.domain.model.plus
import com.sefford.artdrian.downloads.domain.model.progress
import com.sefford.artdrian.downloads.domain.model.total
import com.sefford.artdrian.downloads.store.DownloadsState
import com.sefford.artdrian.notifications.model.Notifications
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.scan

fun StateFlow<DownloadsState>.bridgeNotifications(notify: (Notifications.DownloadNotification) -> Unit, scope: CoroutineScope) =
    filterIsInstance<DownloadsState.Loaded>()
        .map { downloads -> downloads.downloads }
        .scan(emptyList()) { prev: Downloads, next: Downloads ->
            if (prev.isEmpty()) {
                next.filterNot { download -> download is Download.Finished }
            } else {
                prev + next
            }
        }
        .filter { downloads -> downloads.isNotEmpty() }
        .map { downloads ->
            Notifications.DownloadNotification(
                downloads = downloads.size,
                total = downloads.total,
                progress = downloads.progress,
            )
        }.onEach(notify).launchIn(scope)
