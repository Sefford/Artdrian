package com.sefford.artdrian.test

import com.sefford.artdrian.stores.Store
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class StoreStateInstrumentation<Event, State, Effect>(store: Store<Event, State, Effect>, scope: CoroutineScope) {
    private val states: MutableList<State> = mutableListOf()
    private val effects: MutableList<Effect> = mutableListOf()

    init {
        store.state.onEach { states.add(it) }.launchIn(scope)
        store.effects.onEach { effects.add(it) }.launchIn(scope)
    }

    val result: Pair<List<State>, List<Effect>>
        get() = states.toList().drop(1) to effects.toList()
}
