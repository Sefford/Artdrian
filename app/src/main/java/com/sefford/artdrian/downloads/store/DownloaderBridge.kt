package com.sefford.artdrian.downloads.store

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

fun DownloadsStore.bridgeDownloader(
    queue: (String) -> Unit,
    scope: CoroutineScope
) = effects.bridgeDownloader(queue, scope)

fun Flow<DownloadsEffects>.bridgeDownloader(
    queue: (String) -> Unit,
    scope: CoroutineScope
) = filterIsInstance<DownloadsEffects.Register>()
    .onEach { effect -> effect.downloads.forEach { download -> queue(download.url) } }
    .launchIn(scope)
