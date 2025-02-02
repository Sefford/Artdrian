package com.sefford.artdrian.common.language.coroutines

import kotlinx.coroutines.channels.Channel
import java.util.concurrent.ConcurrentLinkedDeque

class DistinctChannel<E, T>(
    private val delegate: Channel<E>,
    private val queue: ConcurrentLinkedDeque<T> = ConcurrentLinkedDeque(),
    private val transform: (E) -> T
) : Channel<E> by delegate {

    override suspend fun send(element: E) {
        val transformed = transform(element)
        if (!queue.contains(transformed)) {
            queue.offer(transformed)
            delegate.send(element)
        }
    }

    override suspend fun receive(): E {
        val element = delegate.receive()
        queue.remove(transform(element))
        return element
    }
}
