package com.sefford.artdrian.stores

interface ReceivesEvents<Event> {
    fun event(event: Event)
}
