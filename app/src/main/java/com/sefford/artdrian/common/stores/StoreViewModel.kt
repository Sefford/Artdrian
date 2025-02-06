package com.sefford.artdrian.common.stores

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

class StoreViewModel<Event, State, Effect>(
    private val logic: StateMachine<Event, State, Effect>,
    private val initial: State,
) : ViewModel(), EffectfulStore<Event, State, Effect> {

    private val store by lazy {
        KotlinStore(logic, initial, viewModelScope)
    }

    override val current: State
        get() = store.current

    override fun event(event: Event) = store.event(event)

    override val state: StateFlow<State>
        get() = store.state

    override val effects: Flow<Effect>
        get() = store.effects

    override fun effect(effect: Effect) =
        store.effect(effect)
}
