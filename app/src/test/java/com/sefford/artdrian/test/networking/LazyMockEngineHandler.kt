package com.sefford.artdrian.test.networking

import io.ktor.client.engine.mock.MockRequestHandler

class LazyMockEngineHandler(
    private var handlers: ArrayDeque<MockRequestHandler> = ArrayDeque()
) {

    fun queue(handler: MockRequestHandler) {
        handlers.add(handler)
    }

    fun next(): MockRequestHandler = if (handlers.isNotEmpty()) handlers.removeFirst() else { request ->
        throw IllegalStateException("There is no behavior configured for a call with ${request.url} ")
    }
}
