package com.sefford.artdrian.connectivity

import android.net.ConnectivityManager
import android.os.Build
import androidx.annotation.RequiresApi

class DefaultConnectivitySubscription(
    private val manager: ConnectivityManager
) : ConnectivitySubscription {

    private var store: ConnectivityStore? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun start(store: ConnectivityStore) {
        this.store = store
        manager.registerDefaultNetworkCallback(store)
    }

    override fun end() {
        store?.let { manager.unregisterNetworkCallback(it) }
    }
}
