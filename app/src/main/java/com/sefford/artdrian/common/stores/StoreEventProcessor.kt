package com.sefford.artdrian.common.stores

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class StoreEventProcessor<Event, State, Effect>(
    val current: () -> State,
    val state: ((State) -> State) -> Unit,
    val effect: (Effect) -> Unit,
    private val logic: StateMachine<Event, State, Effect>,
    private val scope: CoroutineScope
): ReceivesEvents<Event> {

    override fun event(event: Event) {
        scope.launch { logic(event) }
    }
}

