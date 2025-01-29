package com.sefford.artdrian.common.stores

import com.sefford.artdrian.test.FakeLogger
import com.sefford.artdrian.test.unconfine
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class LoggableStateStorageTest {

    private val logger = FakeLogger()

    @Test
    fun `performs logging`() = runTest {
        val storage = StoreStateStorage(1)

        storage.monitor(logger, backgroundScope.unconfine(), TAG)

        storage.state { 2 }

        logger.messages.shouldHaveSize(2).last().should { (tag, message) ->
            tag shouldBe TAG
            message shouldBe LOGGED_MESSAGE
        }
    }
}

private const val TAG = "Test Storage"
private const val LOGGED_MESSAGE = "State Change: 1 -> 2"
