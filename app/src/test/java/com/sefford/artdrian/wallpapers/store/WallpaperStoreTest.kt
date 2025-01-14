package com.sefford.artdrian.wallpapers.store

import com.sefford.artdrian.common.data.DataError
import com.sefford.artdrian.test.StoreInstrumentation
import com.sefford.artdrian.test.mothers.WallpaperMother
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainInOrder
import io.kotest.matchers.collections.shouldContainOnly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.should
import io.kotest.matchers.types.shouldBeInstanceOf
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.plus
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class WallpaperStoreTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val scope = MainScope().plus(UnconfinedTestDispatcher())

    @Test
    fun `attempts to load the wallpapers`() {
        val store = StoreInstrumentation(WallpaperStateMachine, WallpapersState.Idle.Empty)

        store.event(WallpaperEvents.Load)

        store.result.should { (states, effects) ->
            states.shouldBeEmpty()
            effects.shouldContainOnly(WallpaperEffects.LoadAll)
        }
    }

    @Test
    fun `attempts to refresh the wallpapers`() = runTest {
        val store = StoreInstrumentation(WallpaperStateMachine, WallpapersState.Idle.Empty)

        store.event(WallpaperEvents.Refresh)

        store.result.should { (states, effects) ->
            states.shouldHaveSize(1)
            states.first().shouldBeInstanceOf<WallpapersState.Idle>()
            effects.shouldContainInOrder(WallpaperEffects.Clear, WallpaperEffects.LoadAll)
        }
    }

    @Test
    fun `receives an error loading all the wallpapers`() = runTest {
        val store = StoreInstrumentation(WallpaperStateMachine, WallpapersState.Idle.Empty)

        store.event(WallpaperEvents.OnErrorReceived(DataError.Network.Invalid(HttpStatusCode.BadRequest)))

        store.result.should { (states, effects) ->
            states.shouldHaveSize(1)
            states.first().shouldBeInstanceOf<WallpapersState.Idle.OnNetworkError>()
            effects.shouldBeEmpty()
        }
    }

    @Test
    fun `loads wallpapers`() = runTest {
        val store = StoreInstrumentation(WallpaperStateMachine, WallpapersState.Idle.Empty)

        store.event(WallpaperEvents.OnResponseReceived(WallpaperMother.generateNetwork()))

        store.result.should { (states, effects) ->
            states.shouldHaveSize(1)
            states.first().shouldBeInstanceOf<WallpapersState.Loaded>()
            effects.shouldHaveSize(2)
            effects.first().shouldBeInstanceOf<WallpaperEffects.Persist>()
            (effects.first() as WallpaperEffects.Persist).metadata.shouldHaveSize(1)
            effects.last().shouldBeInstanceOf<WallpaperEffects.PrepareDownloads>()
            (effects.last() as WallpaperEffects.PrepareDownloads).downloads.shouldHaveSize(2)
        }
    }

    @Test
    fun `does not persist a cache response`() = runTest {
        val store = StoreInstrumentation(WallpaperStateMachine, WallpapersState.Idle.Empty)

        store.event(WallpaperEvents.OnResponseReceived(WallpaperMother.generateLocal()))

        store.result.should { (states, effects) ->
            states.shouldHaveSize(1)
            states.first().shouldBeInstanceOf<WallpapersState.Loaded>()
            effects.shouldBeEmpty()
        }
    }
}
