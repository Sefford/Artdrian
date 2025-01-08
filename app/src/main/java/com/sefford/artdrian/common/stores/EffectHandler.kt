package com.sefford.artdrian.common.stores

interface EffectHandler<Event, Effect> {
    fun handle(effect: Effect, event: (Event) -> Unit)
}
