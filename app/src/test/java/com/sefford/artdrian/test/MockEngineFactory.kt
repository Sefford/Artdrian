package com.sefford.artdrian.test

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockEngineConfig

class MockEngineFactory(private val handlers: LazyMockEngineHandler) : HttpClientEngineFactory<MockEngineConfig> {

    private val engine: MockEngine = MockEngine { request ->
        handlers.next()(request)
    }

    override fun create(block: MockEngineConfig.() -> Unit): HttpClientEngine = engine
}
