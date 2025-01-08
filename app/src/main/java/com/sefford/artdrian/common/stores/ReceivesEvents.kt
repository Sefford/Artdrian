package com.sefford.artdrian.common.stores

interface ReceivesEvents<Event> {
    fun event(event: Event)
}
