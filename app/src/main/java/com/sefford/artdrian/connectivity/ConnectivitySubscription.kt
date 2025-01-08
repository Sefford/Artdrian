package com.sefford.artdrian.connectivity

interface ConnectivitySubscription {
    fun start(store: ConnectivityStore)

    fun end()
}
