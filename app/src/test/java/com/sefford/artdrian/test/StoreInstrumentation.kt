package com.sefford.artdrian.test

import com.sefford.artdrian.common.stores.ReceivesEvents
import com.sefford.artdrian.common.stores.StateMachine
import com.sefford.artdrian.common.stores.StoreEventProcessor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.plus
import kotlinx.coroutines.test.UnconfinedTestDispatcher

class StoreInstrumentation<Event, State, Effect> @OptIn(ExperimentalCoroutinesApi::class) constructor(
    logic: StateMachine<Event, State, Effect>,
    private val initial: State,
    scope: CoroutineScope = MainScope().plus(UnconfinedTestDispatcher())
) : ReceivesEvents<Event> {
    private val events: MutableList<Event> = mutableListOf()
    private val states: MutableList<State> = mutableListOf()
    private val effects: MutableList<Effect> = mutableListOf()
    private val current: State
        get() = if (states.isEmpty()) initial else states.last()

    private val processor = StoreEventProcessor(
        current = ::current,
        state = { transformation -> transformation(current).also { newState -> states.add(newState) } },
        effect = { effect -> effects.add(effect) },
        logic = logic,
        scope = scope,
    )

    val result: Pair<List<State>, List<Effect>>
        get() = states.toList() to effects.toList()

    val dump: Triple<List<Event>, List<State>, List<Effect>>
        get() = Triple(events.toList(), states.toList(), effects.toList())

    override fun event(event: Event) {
        processor.event(event)
    }
}
