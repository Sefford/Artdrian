package com.sefford.artdrian.data

import com.sefford.artdrian.model.Source
import io.ktor.http.HttpStatusCode

sealed class DataError {

    abstract val source: Source

    sealed class Network : DataError() {
        class NotFound(val id: String = "") : Network()
        class Invalid(val status: Int = 0) : Network() {
            constructor(status: HttpStatusCode) : this(status.value)
        }

        class Critical(val error: Throwable) : Network()

        override val source: Source = Source.NETWORK
    }

    sealed class Local : DataError() {
        class NotFound(val id: String = "") : Local()
        class Critical(val error: Throwable) : Local()

        override val source: Source = Source.LOCAL
    }
}
