package com.sefford.artdrian.stores

interface EffectHandler<Event, Effect> {
    fun handle(effect: Effect, event: (Event) -> Unit)
}
