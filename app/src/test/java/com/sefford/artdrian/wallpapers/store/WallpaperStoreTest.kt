package com.sefford.artdrian.wallpapers.store

import com.sefford.artdrian.data.DataError
import com.sefford.artdrian.model.Source
import com.sefford.artdrian.model.WallpaperList
import com.sefford.artdrian.test.StoreStateInstrumentation
import com.sefford.artdrian.test.mothers.MetadataMother
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
    fun `attempts to load the wallpapers`()  {
        val store = WallpaperStore(WallpapersState.Idle, scope)
        val instrumentation = StoreStateInstrumentation(store, scope)

        store.event(WallpaperEvents.Load)

        instrumentation.result.should { (states, effects) ->
            states.shouldBeEmpty()
            effects.shouldContainOnly(WallpaperEffects.LoadAll)
        }
    }

    @Test
    fun `attempts to refresh the wallpapers`() = runTest {
        val store = WallpaperStore(WallpapersState.Idle, scope)
        val instrumentation = StoreStateInstrumentation(store, scope)

        store.event(WallpaperEvents.Refresh)

        instrumentation.result.should { (states, effects) ->
            states.shouldBeEmpty()
            effects.shouldContainInOrder(WallpaperEffects.Clear, WallpaperEffects.LoadAll)
        }
    }

    @Test
    fun `receives an error loading all the wallpapers`() = runTest {
        val store = WallpaperStore(WallpapersState.Idle, scope)
        val instrumentation = StoreStateInstrumentation(store, scope)

        store.event(WallpaperEvents.OnErrorReceived(DataError.Network.Invalid(HttpStatusCode.BadRequest)))

        instrumentation.result.should { (states, effects) ->
            states.shouldHaveSize(1)
            states.first().shouldBeInstanceOf<WallpapersState.Error>()
            effects.shouldBeEmpty()
        }
    }

    @Test
    fun `loads wallpapers`() = runTest {
        val store = WallpaperStore(WallpapersState.Idle, scope)
        val instrumentation = StoreStateInstrumentation(store, scope)

        store.event(WallpaperEvents.OnResponseReceived(WallpaperList(MetadataMother.generate(), Source.NETWORK)))

        instrumentation.result.should { (states, effects) ->
            states.shouldHaveSize(1)
            states.first().shouldBeInstanceOf<WallpapersState.Loaded>()
            effects.shouldHaveSize(1)
            effects.first().shouldBeInstanceOf<WallpaperEffects.Persist>()
            (effects.first() as WallpaperEffects.Persist).metadata.shouldHaveSize(1)
        }
    }
}
