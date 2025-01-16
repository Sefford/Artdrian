package com.sefford.artdrian.common.stores

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class StoreStateStorage<State>(initial: State) : HoldsState<State> {
    private val _state = MutableStateFlow(initial)
    override val state: StateFlow<State> = _state.asStateFlow()

    override val current: State
        get() = state.value

    fun state(block: (State) -> State) = _state.update(block)

}
