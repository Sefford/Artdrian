package com.sefford.artdrian.common.stores

typealias StateMachine<Event, State, Effect> = StoreEventProcessor<Event, State, Effect>.(Event) -> Unit
