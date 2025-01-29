package com.sefford.artdrian.common.stores

import com.sefford.artdrian.common.utils.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.plus

class KotlinStore<Event, State, Effect> private constructor(
    private val stateStorage: HoldsState<State>,
    private val effectDispatching: DispatchesEffects<Effect>,
    private val eventRecipient: ReceivesEvents<Event>,
    private val scope: CoroutineScope
) : EffectfulStore<Event, State, Effect>,
    ReceivesEvents<Event> by eventRecipient,
    HoldsState<State> by stateStorage,
    DispatchesEffects<Effect> by effectDispatching {

    fun monitor(logger: Logger, tag: String): KotlinStore<Event, State, Effect> {
        val storage = stateStorage.monitor(logger, scope, tag)
        val dispatching = effectDispatching.monitor(logger, scope, tag)
        val processor = eventRecipient + logger + tag
        return KotlinStore(
            storage,
            dispatching,
            processor,
            scope,
        )
    }

    companion object {
        operator fun <Event, State, Effect> invoke(
            logic: StateMachine<Event, State, Effect>,
            initial: State,
            scope: CoroutineScope = MainScope().plus(Dispatchers.Default)
        ): KotlinStore<Event, State, Effect> {
            val storage = StoreStateStorage(initial)
            val dispatching = StoreEffectDispatching<Effect>(scope)
            val processor = StoreEventProcessor(
                current = { storage.current },
                state = storage::state,
                effect = dispatching::effect,
                logic = logic,
                scope = scope
            )
            return KotlinStore(storage, dispatching, processor, scope)
        }
    }
}

