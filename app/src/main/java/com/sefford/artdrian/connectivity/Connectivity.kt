package com.sefford.artdrian.connectivity

import android.net.NetworkCapabilities
import kotlin.math.roundToInt

sealed class Connectivity(
    val type: Type,
    val speed: Speed
) {

    data object Undetermined : Connectivity(Type.DISCONNECTED, Speed.Disconnected)

    data object Disconnected : Connectivity(Type.DISCONNECTED, Speed.Disconnected)

    class Connected(type: Type, speed: Speed) : Connectivity(type, speed) {

        val metered: Boolean by lazy { type == Type.MOBILE }

    }

    enum class Type {
        DISCONNECTED, MOBILE, WIFI, ETHERNET, OTHER;

        companion object {
            operator fun invoke(capabilities: NetworkCapabilities): Type = when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> WIFI
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> MOBILE
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> ETHERNET
                else -> OTHER
            }
        }
    }

    sealed class Speed(val speed: Int) {

        data object Disconnected : Speed(0) {
            override val parallelDownloads: Int = 0
        }

        class Slow(speed: Int) : Speed(speed) {
            override val parallelDownloads: Int = 1
        }

        class Medium(speed: Int) : Speed(speed) {
            override val parallelDownloads: Int = (speed / 7).coerceAtMost(2)
        }

        class Fast(speed: Int) : Speed(speed) {
            override val parallelDownloads: Int = (speed / 7).coerceAtMost(4)
        }

        abstract val parallelDownloads: Int

        companion object {
            operator fun invoke(kbps: Int): Speed {
                val speedMbps = (kbps / 1000.0).roundToInt()

                return when {
                    speedMbps >= 56 -> ::Fast
                    speedMbps >= 18.64 -> ::Medium
                    else -> ::Slow
                }(speedMbps)
            }
        }
    }

    companion object {
        operator fun invoke(capabilities: NetworkCapabilities): Connectivity =
            Connected(type = Type(capabilities), speed = Speed(capabilities.linkDownstreamBandwidthKbps))
    }
}
