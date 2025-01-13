package com.sefford.artdrian.connectivity

import android.net.NetworkCapabilities
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.flow.flow
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf

@RunWith(RobolectricTestRunner::class)
class ConnectivityTest {

    @Test
    fun `creates a WiFi connectivity`() {
        val capabilities = shadowOf(NetworkCapabilities()).addTransportType(NetworkCapabilities.TRANSPORT_WIFI)

        Connectivity(capabilities).type shouldBe Connectivity.Type.WIFI
    }

    @Test
    fun `creates a mobile connectivity`() {
        val capabilities = shadowOf(NetworkCapabilities()).addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)

        Connectivity(capabilities).type shouldBe Connectivity.Type.MOBILE
    }

    @Test
    fun `creates an ethernet connectivity`() {
        val capabilities = shadowOf(NetworkCapabilities()).addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)

        Connectivity(capabilities).type shouldBe Connectivity.Type.ETHERNET
    }

    @Test
    fun `creates other connectivity`() {
        val capabilities = shadowOf(NetworkCapabilities()).addTransportType(NetworkCapabilities.TRANSPORT_BLUETOOTH)

        Connectivity(capabilities).type shouldBe Connectivity.Type.OTHER
    }

    @Test
    fun `creates low speed connectivity`() {
        val capabilities = shadowOf(NetworkCapabilities()).setLinkDownstreamBandwidthKbps(LOW_SPEED)

        Connectivity(capabilities).speed.shouldBeInstanceOf<Connectivity.Speed.Slow>()
    }

    @Test
    fun `creates medium speed connectivity`() {
        val capabilities = shadowOf(NetworkCapabilities()).setLinkDownstreamBandwidthKbps(MEDIUM_SPEED)

        Connectivity(capabilities).speed.shouldBeInstanceOf<Connectivity.Speed.Medium>()
    }

    @Test
    fun `creates fast speed connectivity`() {
        val capabilities = shadowOf(NetworkCapabilities()).setLinkDownstreamBandwidthKbps(HIGH_SPEED)

        Connectivity(capabilities).speed.shouldBeInstanceOf<Connectivity.Speed.Fast>()
    }

    @Test
    fun `reports metered connection`() {
        val capabilities = shadowOf(NetworkCapabilities()).addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)

        Connectivity(capabilities).shouldBeInstanceOf<Connectivity.Connected>().metered.shouldBeTrue()
    }

    @Test
    fun `reports unmetered connection`() {
        val capabilities = shadowOf(NetworkCapabilities()).addTransportType(NetworkCapabilities.TRANSPORT_WIFI)

        Connectivity(capabilities).shouldBeInstanceOf<Connectivity.Connected>().metered.shouldBeFalse()
    }
}

private const val LOW_SPEED = 10_000
private const val MEDIUM_SPEED = 25_000
private const val HIGH_SPEED = 100_000
