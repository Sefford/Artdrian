package com.sefford.artdrian.downloads.store

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

fun Flow<String>.bridgeDownloader(
    queue: (String) -> Unit,
    scope: CoroutineScope
) = onEach { download -> queue(download) }
    .launchIn(scope)
