package com.sefford.artdrian.notifications.effects

import com.sefford.artdrian.notifications.store.NotificationsStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

fun NotificationsStore.bridgeEffectHandler(
    effectHandler: NotificationsEffectHandler,
    scope: CoroutineScope
) {
    effects.onEach { effect -> effectHandler.handle(effect) }.launchIn(scope)
}
