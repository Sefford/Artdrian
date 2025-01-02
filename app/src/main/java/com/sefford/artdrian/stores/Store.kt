package com.sefford.artdrian.stores

interface Store<Event, State> : ReceivesEvents<Event>, HoldsState<State>
