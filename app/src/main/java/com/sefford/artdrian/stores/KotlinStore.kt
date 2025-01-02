package com.sefford.artdrian.stores

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.plus

class KotlinStore<Event, State, Effect> private constructor(
    logic: StateMachine<Event, State, Effect>,
    scope: CoroutineScope,
    stateStorage: StoreStateStorage<State>,
    effectDispatching: StoreEffectDispatching<Effect>,
    eventRecipient: StoreEventProcessor<Event, State, Effect> = StoreEventProcessor(
        current = { stateStorage.current },
        state = stateStorage::state,
        effect = effectDispatching::effect,
        logic = logic,
        scope = scope
    ),
) : EffectfulStore<Event, State, Effect>,
    ReceivesEvents<Event> by eventRecipient,
    HoldsState<State> by stateStorage,
    DispatchesEffects<Effect> by effectDispatching {

    constructor(
        logic: StateMachine<Event, State, Effect>,
        initial: State,
        scope: CoroutineScope = MainScope().plus(Dispatchers.Default)
    ) : this(logic, scope, StoreStateStorage<State>(initial), StoreEffectDispatching(scope))
}
