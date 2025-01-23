package com.sefford.artdrian.downloads.store

import com.sefford.artdrian.downloads.domain.model.Download
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

fun StateFlow<DownloadsState>.bridgeDownloader(
    queue: (Download) -> Unit,
    scope: CoroutineScope
) = filterIsInstance<DownloadsState.Loaded>()
    .map { state -> state.downloads.filterNot { download -> download is Download.Finished } }
    .onEach { downloads -> downloads.forEach { download -> queue(download) } }
    .launchIn(scope)
