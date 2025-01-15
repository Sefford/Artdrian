package com.sefford.artdrian.downloads.store

import com.sefford.artdrian.common.data.DataError
import com.sefford.artdrian.test.StoreInstrumentation
import com.sefford.artdrian.test.mothers.DownloadsMother
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.should
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.jupiter.api.Test

class DownloadsStoreTest {

    @Test
    fun `attempts to load the downloads`() {
        val store = StoreInstrumentation(DownloadsStateMachine, DownloadsState.Idle)

        store.event(DownloadsEvents.LoadAll)

        store.result.should { (states, effects) ->
            states.shouldBeEmpty()
            effects.shouldHaveSize(1)
            effects.first().shouldBeInstanceOf<DownloadsEffects.LoadAll>()
        }
    }

    @Test
    fun `receives preloads`() {
        val store = StoreInstrumentation(DownloadsStateMachine, DownloadsState.Idle)

        store.event(DownloadsEvents.Register(listOf(DownloadsMother.createPending())))

        store.result.should { (states, effects) ->
            states.last().shouldBeInstanceOf<DownloadsState.Preload>()
            (states.last() as DownloadsState.Preload).downloads.shouldHaveSize(1)
            effects.shouldBeEmpty()
        }
    }

    @Test
    fun `receives downloads`() {
        val store = StoreInstrumentation(DownloadsStateMachine, DownloadsState.Idle)

        store.event(DownloadsEvents.OnDownloadsReceived(listOf(DownloadsMother.createPending())))

        store.result.should { (states, effects) ->
            states.last().shouldBeInstanceOf<DownloadsState.Loaded>()
            (states.last() as DownloadsState.Loaded).downloads.shouldHaveSize(1)
            effects.shouldHaveSize(1)
            effects.first().shouldBeInstanceOf<DownloadsEffects.Register>()
            (effects.first() as DownloadsEffects.Register).downloads.shouldHaveSize(1)
        }
    }

    @Test
    fun `receives error`() {
        val store = StoreInstrumentation(DownloadsStateMachine, DownloadsState.Idle)

        store.event(DownloadsEvents.OnErrorReceived(DataError.Local.Empty))

        store.result.should { (states, effects) ->
            states.last().shouldBeInstanceOf<DownloadsState.Empty>()
            effects.shouldBeEmpty()
        }
    }

    @Test
    fun `triggers persistence when receiving a registration`() {
        val store = StoreInstrumentation(DownloadsStateMachine, DownloadsState.Empty)

        store.event(DownloadsEvents.Register(listOf(DownloadsMother.createPending())))

        store.result.should { (states, effects) ->
            states.last().shouldBeInstanceOf<DownloadsState.Loaded>()
            (states.last() as DownloadsState.Loaded).downloads.shouldHaveSize(1)
            effects.shouldHaveSize(1)
            effects.first().shouldBeInstanceOf<DownloadsEffects.Register>()
            (effects.first() as DownloadsEffects.Register).downloads.shouldHaveSize(1)
        }
    }

    @Test
    fun `triggers persistence when receiving an error`() {
        val store = StoreInstrumentation(DownloadsStateMachine, DownloadsState.Preload(listOf(DownloadsMother.createPending())))

        store.event(DownloadsEvents.OnErrorReceived(DataError.Local.Empty))

        store.result.should { (states, effects) ->
            states.last().shouldBeInstanceOf<DownloadsState.Loaded>()
            (states.last() as DownloadsState.Loaded).downloads.shouldHaveSize(1)
            effects.shouldHaveSize(1)
            effects.first().shouldBeInstanceOf<DownloadsEffects.Register>()
            (effects.first() as DownloadsEffects.Register).downloads.shouldHaveSize(1)
        }
    }
}
