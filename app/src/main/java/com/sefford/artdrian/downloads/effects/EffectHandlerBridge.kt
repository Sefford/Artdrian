package com.sefford.artdrian.downloads.effects

import com.sefford.artdrian.downloads.store.DownloadsStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

fun DownloadsStore.bridgeEffectHandler(
    domainEffectHandler: DownloadsDomainEffectHandler,
    ioScope: CoroutineScope
) {
    effects.onEach { effect -> domainEffectHandler.handle(effect, ::event) }
        .launchIn(ioScope)
}
