package com.sefford.artdrian.downloads.store

import com.sefford.artdrian.common.data.DataError
import com.sefford.artdrian.test.StoreInstrumentation
import com.sefford.artdrian.test.mothers.DownloadsMother
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainOnly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.jupiter.api.Test

class DownloadsStoreTest {

    @Test
    fun `attempts to load the downloads`() {
        val store = StoreInstrumentation(DownloadsStateMachine, DownloadsState.Idle)

        store.event(DownloadsEvents.LoadAll)

        store.result.should { (states, effects) ->
            states.shouldBeEmpty()
            effects.shouldContainOnly(DownloadsEffects.LoadAll)
        }
    }

    @Test
    fun `attempts to update a download`() {
        val store = StoreInstrumentation(DownloadsStateMachine, DownloadsState.Idle)
        val download = DownloadsMother.createOngoing()

        store.event(DownloadsEvents.Update(download))

        store.result.should { (states, effects) ->
            states.shouldBeEmpty()
            effects.last().shouldBeInstanceOf<DownloadsEffects.Update>()
                .download shouldBe download
        }
    }

    @Test
    fun `holds the preloads when not having confirmation from the backend`() {
        val store = StoreInstrumentation(DownloadsStateMachine, DownloadsState.Idle)

        store.event(DownloadsEvents.Register(listOf(FIRST)))

        store.result.should { (states, effects) ->
            states.last().shouldBeInstanceOf<DownloadsState.Preload>()
                .downloads.shouldContainOnly(FIRST)
            effects.shouldBeEmpty()
        }
    }

    @Test
    fun `merges preloads when receiving more preloads`() {
        val store = StoreInstrumentation(DownloadsStateMachine, DownloadsState.Preload(listOf(FIRST)))
        store.event(DownloadsEvents.Register(listOf(SECOND)))

        store.result.should { (states, effects) ->
            states.last().shouldBeInstanceOf<DownloadsState.Preload>()
                .downloads.shouldContainOnly(FIRST, SECOND)
            effects.shouldBeEmpty()
        }
    }

    @Test
    fun `persists the new downloads when the state is empty`() {
        val store = StoreInstrumentation(DownloadsStateMachine, DownloadsState.Empty)

        store.event(DownloadsEvents.Register(listOf(FIRST, SECOND)))

        store.result.should { (states, effects) ->
            store.current.shouldBeInstanceOf<DownloadsState.Empty>()
            effects.shouldHaveSize(1)
                .last().shouldBeInstanceOf<DownloadsEffects.Register>()
                .downloads.shouldContainOnly(FIRST, SECOND)
        }
    }

    @Test
    fun `persists the new downloads when the state is loaded`() {
        val store = StoreInstrumentation(
            DownloadsStateMachine, DownloadsState.Loaded(
                listOf(SECOND, THIRD)
            )
        )

        store.event(DownloadsEvents.Register(listOf(FIRST, SECOND)))

        store.result.should { (_, effects) ->
            store.current.shouldBeInstanceOf<DownloadsState.Loaded>()
                .downloads.shouldContainOnly(SECOND, THIRD)
            effects.shouldHaveSize(1)
                .last().shouldBeInstanceOf<DownloadsEffects.Register>()
                .downloads.shouldContainOnly(FIRST)
        }
    }

    @Test
    fun `receives the downloads from the cache while on Idle`() {
        val store = StoreInstrumentation(DownloadsStateMachine, DownloadsState.Idle)

        store.event(DownloadsEvents.OnDownloadsReceived(listOf(FIRST)))

        store.result.should { (states, effects) ->
            states.last().shouldBeInstanceOf<DownloadsState.Loaded>()
                .downloads.shouldContainOnly(FIRST)
            effects.shouldHaveSize(1)
                .last().shouldBeInstanceOf<DownloadsEffects.Notify>()
                .downloads.shouldContainOnly(FIRST)
        }
    }

    @Test
    fun `receives the downloads from the cache while on Empty`() {
        val store = StoreInstrumentation(DownloadsStateMachine, DownloadsState.Empty)

        store.event(DownloadsEvents.OnDownloadsReceived(listOf(FIRST)))

        store.result.should { (states, effects) ->
            states.last().shouldBeInstanceOf<DownloadsState.Loaded>()
                .downloads.shouldContainOnly(FIRST)
            effects.shouldHaveSize(1)
                .last().shouldBeInstanceOf<DownloadsEffects.Notify>()
                .downloads.shouldContainOnly(FIRST)
        }
    }

    @Test
    fun `receives the downloads from the cache while on Preload`() {
        val store = StoreInstrumentation(
            DownloadsStateMachine,
            DownloadsState.Preload(listOf(FIRST, SECOND))
        )

        store.event(DownloadsEvents.OnDownloadsReceived(listOf(SECOND, THIRD)))

        store.result.should { (states, effects) ->
            states.last().shouldBeInstanceOf<DownloadsState.Loaded>()
                .downloads.shouldContainOnly(SECOND, THIRD)
            effects.shouldHaveSize(2)
            effects.first().shouldBeInstanceOf<DownloadsEffects.Notify>()
                .downloads.shouldContainOnly(SECOND, THIRD)
            effects.last().shouldBeInstanceOf<DownloadsEffects.Register>()
                .downloads.shouldContainOnly(FIRST)
        }
    }

    @Test
    fun `receives the downloads from the cache while on Loaded`() {
        val store = StoreInstrumentation(
            DownloadsStateMachine,
            DownloadsState.Loaded(listOf(FIRST, SECOND))
        )

        store.event(DownloadsEvents.OnDownloadsReceived(listOf(SECOND, THIRD)))

        store.result.should { (states, effects) ->
            states.last().shouldBeInstanceOf<DownloadsState.Loaded>()
                .downloads.shouldContainOnly(SECOND, THIRD)
            effects.shouldHaveSize(1)
                .last().shouldBeInstanceOf<DownloadsEffects.Notify>()
                .downloads.shouldContainOnly(THIRD)
        }
    }

    @Test
    fun `receives an error`() {
        val store = StoreInstrumentation(DownloadsStateMachine, DownloadsState.Idle)

        store.event(DownloadsEvents.OnErrorReceived(DataError.Local.Empty))

        store.result.should { (states, effects) ->
            states.last().shouldBeInstanceOf<DownloadsState.Empty>()
        }
    }
}

private val FIRST = DownloadsMother.createPending()
private val SECOND = DownloadsMother.createPending("http://image.com/2/example.jpg")
private val THIRD = DownloadsMother.createPending("http://image.com/3/example.jpg")

