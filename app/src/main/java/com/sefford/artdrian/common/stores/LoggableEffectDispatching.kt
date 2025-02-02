package com.sefford.artdrian.common.stores

import com.sefford.artdrian.common.utils.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class LoggableEffectDispatching<Effect>(
    private val delegate: DispatchesEffects<Effect>,
    private val logger: Logger,
    scope: CoroutineScope,
    private val tag: String
) : DispatchesEffects<Effect> by delegate {

    init {
        delegate.effects.onEach { effect -> logger.log(tag, "Dispatched Effect: $effect") }
            .launchIn(scope)
    }
}

fun <Effect> DispatchesEffects<Effect>.monitor(logger: Logger, scope: CoroutineScope, tag: String) =
    LoggableEffectDispatching(this, logger, scope, tag)
