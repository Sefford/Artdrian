package com.sefford.artdrian.common.stores

interface EffectfulStore<Event, State, Effect> : Store<Event, State>, DispatchesEffects<Effect>
