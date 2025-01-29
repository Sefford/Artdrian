package com.sefford.artdrian.common.stores

import com.sefford.artdrian.common.utils.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.scan

class LoggableStateStorage<State>(
    private val delegate: HoldsState<State>,
    private val logger: Logger,
    private val scope: CoroutineScope,
    private val tag: String
) : HoldsState<State> by delegate {

    init {
        state.scan(current) { prev, next ->
            logger.debug(tag, "State Change: $prev -> $next")
            next
        }.launchIn(scope)
    }
}

fun <State> HoldsState<State>.monitor(logger: Logger, scope: CoroutineScope, tag: String) =
    LoggableStateStorage(this, logger, scope, tag)


