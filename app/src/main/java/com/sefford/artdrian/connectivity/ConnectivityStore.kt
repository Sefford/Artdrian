package com.sefford.artdrian.connectivity

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import com.sefford.artdrian.common.stores.HoldsState
import com.sefford.artdrian.common.stores.StoreStateStorage

class ConnectivityStore(
    private val storage: StoreStateStorage<Connectivity>
) : ConnectivityManager.NetworkCallback(), HoldsState<Connectivity> by storage {

    constructor(initial: Connectivity) : this(StoreStateStorage(initial))

    override fun onUnavailable() {
        storage.state { Connectivity.Disconnected }
    }

    override fun onCapabilitiesChanged(network: Network, capabilities: NetworkCapabilities) {
        storage.state { Connectivity(capabilities) }
    }
}
