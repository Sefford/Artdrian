package com.sefford.artdrian.common.data

import arrow.core.Either
import com.sefford.artdrian.wallpapers.domain.model.Sourced
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.http.HttpStatusCode
import java.net.SocketTimeoutException
import java.nio.channels.UnresolvedAddressException

sealed class DataError : Sourced {
    sealed class Local : DataError(), Sourced by Sourced.Local {
        data object Empty: Local()
        class NotFound(val id: String = "") : Local()
        class Critical(val error: Throwable) : Local()
    }

    sealed class Network : DataError(), Sourced by Sourced.Network {
        class NotFound(val id: String = "") : Network()
        class Invalid(val status: Int = 0) : Network() {
            constructor(status: HttpStatusCode) : this(status.value)
        }
        data object NoConnection: Network()
        data object ConnectTimeout: Network()
        data object SocketTimeout: Network()
        class Critical(val error: Throwable) : Network()

    }
}

fun <R> Either<Throwable, R>.toDataError() = mapLeft { error ->
    when (error) {
        is UnresolvedAddressException -> DataError.Network.NoConnection
        is ConnectTimeoutException -> DataError.Network.ConnectTimeout
        is SocketTimeoutException -> DataError.Network.SocketTimeout
        else -> DataError.Local.Critical(error)
    }
}
