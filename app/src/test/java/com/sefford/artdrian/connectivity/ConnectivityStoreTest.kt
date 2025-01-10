package com.sefford.artdrian.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.test.core.app.ApplicationProvider
import com.sefford.artdrian.common.stores.StoreStateStorage
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf

@RunWith(RobolectricTestRunner::class)
class ConnectivityStoreTest {

    private val cm =
        ApplicationProvider.getApplicationContext<Context>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val storage = StoreStateStorage<Connectivity>(Connectivity.Undetermined)
    private val store = ConnectivityStore(storage = storage)

    @Test
    fun `reports capabilities change`() {
        val capabilities = shadowOf(NetworkCapabilities()).apply {
            addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        }.setLinkDownstreamBandwidthKbps(HIGH_SPEED)


        store.onCapabilitiesChanged(cm.activeNetwork!!, capabilities)

        store.state.value.shouldBeInstanceOf<Connectivity.Connected>().should { connectivity ->
            connectivity.type shouldBe Connectivity.Type.WIFI
            connectivity.speed.shouldBeInstanceOf<Connectivity.Speed.Fast>()
        }
    }

    @Test
    fun `reports disconnection`() {
        store.onUnavailable()

        store.state.value.shouldBeInstanceOf<Connectivity.Disconnected>()
    }

}

private const val HIGH_SPEED = 100_000

