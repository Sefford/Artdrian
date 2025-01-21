package com.sefford.artdrian.test.networking

import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.InternalAPI
import kotlinx.io.Source
import java.io.Closeable

class FakeByteReadChannel(
    private val onReadSource: () -> Source = { throw IllegalStateException() },
    private var closed: Boolean = false,
    private var reason: Throwable = IllegalStateException()
) : ByteReadChannel, Closeable {

    override val closedCause: Throwable
        get() = reason

    override val isClosedForRead: Boolean
        get() = closed

    @InternalAPI
    override val readBuffer: Source
        get() = onReadSource()

    override suspend fun awaitContent(min: Int): Boolean = false

    override fun cancel(cause: Throwable?) {
        reason = cause ?: NoSpecifiedCause()
        closed = true
    }

    override fun close() {
        reason = ClosedNormally()
        closed = true
    }

    class NoSpecifiedCause: Exception()

    class ClosedNormally: Exception()
}
