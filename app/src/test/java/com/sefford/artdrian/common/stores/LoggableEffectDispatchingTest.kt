package com.sefford.artdrian.common.stores

import com.sefford.artdrian.test.FakeLogger
import com.sefford.artdrian.test.unconfine
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class LoggableEffectDispatchingTest {

    private val logger = FakeLogger()

    @Test
    fun `performs logging`() = runTest {
        val storage = StoreEffectDispatching<Int>(backgroundScope.unconfine())

        storage.monitor(logger, backgroundScope.unconfine(), TAG)

        storage.effect(1)

        logger.messages.shouldHaveSize(1).last().should { (tag, message) ->
            tag shouldBe TAG
            message shouldBe LOGGED_MESSAGE
        }
    }
}

private const val TAG = "Test Dispatcher"
private const val LOGGED_MESSAGE = "Dispatched Effect: 1"
