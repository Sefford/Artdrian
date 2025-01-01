package com.sefford.artdrian.stores

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class Store<Event, State, Effect>(
    initial: State,
    private val scope: CoroutineScope
) {

    private val _state = MutableStateFlow(initial)
    private val _effects = MutableSharedFlow<Effect>()

    val state: StateFlow<State> = _state.asStateFlow()
    val effects: Flow<Effect> = _effects.asSharedFlow()

    fun event(event: Event) { scope.launch { handleEvent(event) } }

    protected abstract fun handleEvent(event: Event)

    protected fun state(block: (State) -> State) = _state.update(block)

    protected fun effect(effect: Effect) = scope.launch { _effects.emit(effect) }
}
