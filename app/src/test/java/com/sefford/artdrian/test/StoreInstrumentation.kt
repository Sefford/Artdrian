package com.sefford.artdrian.test

import arrow.core.NonEmptyList
import arrow.core.nonEmptyListOf
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
    initial: State,
    scope: CoroutineScope = MainScope().plus(UnconfinedTestDispatcher())
) : ReceivesEvents<Event> {
    private val events: MutableList<Event> = mutableListOf()
    private var states: NonEmptyList<State> = nonEmptyListOf(initial)
    private val effects: MutableList<Effect> = mutableListOf()
    val current: State
        get() = states.last()

    private val processor = StoreEventProcessor(
        current = ::current,
        state = { transformation -> transformation(current).also { newState -> states += newState } },
        effect = effects::add,
        logic = logic,
        scope = scope,
    )

    val result: Pair<List<State>, List<Effect>>
        get() = states.tail to effects.toList()

    val dump: Triple<List<Event>, List<State>, List<Effect>>
        get() = Triple(events.toList(), states.toList(), effects.toList())

    override fun event(event: Event) {
        processor.event(event)
    }
}
