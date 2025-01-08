package com.sefford.artdrian.common.stores

interface Store<Event, State> : ReceivesEvents<Event>, HoldsState<State>
