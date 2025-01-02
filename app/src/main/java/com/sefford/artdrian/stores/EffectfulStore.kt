package com.sefford.artdrian.stores

interface EffectfulStore<Event, State, Effect> : Store<Event, State>, DispatchesEffects<Effect>
