package com.sefford.artdrian.common.data

import com.sefford.artdrian.wallpapers.domain.model.Sourced
import io.ktor.http.HttpStatusCode

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
