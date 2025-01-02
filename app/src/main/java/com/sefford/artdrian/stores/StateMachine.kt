package com.sefford.artdrian.stores

typealias StateMachine<Event, State, Effect> = StoreEventProcessor<Event, State, Effect>.(Event) -> Unit
