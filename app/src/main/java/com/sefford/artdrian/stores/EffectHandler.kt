package com.sefford.artdrian.stores

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

abstract class EffectHandler<Event, Effect>(
    protected val scope: CoroutineScope = MainScope().plus(Dispatchers.IO)
) {

    fun handle(effect: Effect, event: (Event) -> Unit) {
        scope.launch {
            handleEffect(effect, event)
        }
    }

    protected abstract suspend fun handleEffect(effect: Effect, event: (Event) -> Unit)
}
