package com.sefford.artdrian.wallpapers.store

import com.sefford.artdrian.data.DataError
import com.sefford.artdrian.model.Sourced
import com.sefford.artdrian.test.mothers.WallpaperMother
import io.kotest.assertions.arrow.core.shouldBeNone
import io.kotest.assertions.arrow.core.shouldBeSome
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.ktor.http.HttpStatusCode
import org.junit.jupiter.api.Test

class LoadedWallpaperStateTest {

    @Test
    fun `merges with an empty state with a local response`() {
        val state = WallpapersState.Loaded(emptyList(), Sourced.Local)

        (state + WallpaperMother.generateLocal()).should { newState ->
            newState.shouldBeInstanceOf<WallpapersState.Loaded>()
            newState.wallpapers.first().source.local.shouldBeTrue()
            newState.source.local.shouldBeTrue()
            newState.next shouldBe WallpapersState.Idle
        }
    }

    @Test
    fun `merges with an empty state with a network response`() {
        val state = WallpapersState.Loaded(emptyList(), Sourced.Local)

        (state + WallpaperMother.generateNetwork()).should { newState ->
            newState.shouldBeInstanceOf<WallpapersState.Loaded>()
            newState.wallpapers.first().source.network.shouldBeTrue()
            newState.source.network.shouldBeTrue()
            newState.next shouldBe WallpapersState.Idle
        }
    }

    @Test
    fun `overrides a local metadata with a network metadata`() {
        val state = WallpapersState.Loaded(WallpaperMother.generateLocal())

        (state + WallpaperMother.generateNetwork()).should { newState ->
            newState.shouldBeInstanceOf<WallpapersState.Loaded>()
            newState.wallpapers.first().source.network.shouldBeTrue()
            newState.source.network.shouldBeTrue()
            newState.next shouldBe WallpapersState.Idle
        }
    }

    @Test
    fun `overrides a network metadata with a network metadata`() {
        val state = WallpapersState.Loaded(WallpaperMother.generateNetwork())

        (state + WallpaperMother.generateNetwork(downloads = UPDATED_DOWNLOADS)).should { newState ->
            newState.shouldBeInstanceOf<WallpapersState.Loaded>()
            newState.wallpapers.first().source.network.shouldBeTrue()
            newState.wallpapers.first().downloads shouldBe UPDATED_DOWNLOADS
            newState.source.network.shouldBeTrue()
            newState.next shouldBe WallpapersState.Idle
        }
    }

    @Test
    fun `overrides a local metadata with a local metadata`() {
        val state = WallpapersState.Loaded(WallpaperMother.generateLocal())

        (state + WallpaperMother.generateLocal(downloads = UPDATED_DOWNLOADS)).should { newState ->
            newState.shouldBeInstanceOf<WallpapersState.Loaded>()
            newState.wallpapers.first().source.local.shouldBeTrue()
            newState.wallpapers.first().downloads shouldBe UPDATED_DOWNLOADS
            newState.source.local.shouldBeTrue()
            newState.next shouldBe WallpapersState.Idle
        }
    }

    @Test
    fun `does not a network metadata with a local metadata`() {
        val state = WallpapersState.Loaded(WallpaperMother.generateNetwork())

        (state + WallpaperMother.generateLocal()).should { newState ->
            newState.shouldBeInstanceOf<WallpapersState.Loaded>()
            newState.wallpapers.first().source.network.shouldBeTrue()
            newState.source.network.shouldBeTrue()
            newState.next shouldBe WallpapersState.Idle
        }
    }

    @Test
    fun `merges with a single element`() {
        val state = WallpapersState.Loaded(emptyList(), Sourced.Local)

        (state + WallpaperMother.generateLocal()).should { newState ->
            newState.shouldBeInstanceOf<WallpapersState.Loaded>()
            newState.wallpapers.first().source.local.shouldBeTrue()
            newState.source.local.shouldBeTrue()
            newState.next shouldBe WallpapersState.Idle
        }
    }

    @Test
    fun `merges with an error of the same type`() {
        val state = WallpapersState.Loaded(
            WallpaperMother.generateLocal().toList().wallpapers,
            WallpapersState.Error(DataError.Network.Invalid(HttpStatusCode.BadRequest.value))
        )

        (state + DataError.Network.NotFound("123")).should { newState ->
            newState.shouldBeInstanceOf<WallpapersState.Loaded>()
            newState.wallpapers.first().source.local.shouldBeTrue()
            newState.source.network.shouldBeTrue()
            newState.next.shouldBeInstanceOf<WallpapersState.Error>()
            (newState.next as WallpapersState.Error).error.shouldBeInstanceOf<DataError.Network.NotFound>()
        }
    }

    @Test
    fun `overrides a local error`() {
        val state = WallpapersState.Loaded(
            WallpaperMother.generateLocal().toList().wallpapers,
            WallpapersState.Error(DataError.Local.NotFound("400"))
        )

        (state + DataError.Network.Invalid(HttpStatusCode.BadRequest.value)).should { newState ->
            newState.shouldBeInstanceOf<WallpapersState.Loaded>()
            newState.wallpapers.first().source.local.shouldBeTrue()
            newState.source.network.shouldBeTrue()
            newState.next.shouldBeInstanceOf<WallpapersState.Error>()
            (newState.next as WallpapersState.Error).error.shouldBeInstanceOf<DataError.Network.Invalid>()
        }
    }

    @Test
    fun `discards a local error`() {
        val state = WallpapersState.Loaded(
            wallpapers = WallpaperMother.generateNetwork().toList().wallpapers,
            sourced = Sourced.Network,
            next = WallpapersState.Error(DataError.Network.Invalid(HttpStatusCode.BadRequest.value))
        )

        (state + DataError.Local.NotFound("123")).should { newState ->
            newState.shouldBeInstanceOf<WallpapersState.Loaded>()
            newState.wallpapers.first().source.network.shouldBeTrue()
            newState.source.network.shouldBeTrue()
            newState.next.shouldBeInstanceOf<WallpapersState.Error>()
            (newState.next as WallpapersState.Error).error.shouldBeInstanceOf<DataError.Network.Invalid>()
        }
    }

    @Test
    fun `transient items are the ones from network`() {
        WallpapersState.Loaded(
            listOf(
                WallpaperMother.generateLocal(),
                WallpaperMother.generateNetwork()
            ),
            Sourced.Network,
            WallpapersState.Error(DataError.Network.Invalid(HttpStatusCode.BadRequest.value))
        ).transient.shouldHaveSize(1)
    }

    @Test
    fun `transient items are empty if the response is from Cache`() {
        WallpapersState.Loaded(
            listOf(
                WallpaperMother.generateLocal(),
                WallpaperMother.generateLocal()
            ),
            Sourced.Network,
            WallpapersState.Error(DataError.Network.Invalid(HttpStatusCode.BadRequest.value))
        ).transient.shouldBeEmpty()
    }

    @Test
    fun `returns a wallpaper when it is loaded`() {
        WallpapersState.Loaded(WallpaperMother.generateLocal(id = WALLPAPER_ID))[WALLPAPER_ID].shouldBeSome()
    }

    @Test
    fun `returns a none when the wallpaper does not exist`() {
        WallpapersState.Loaded(WallpaperMother.generateLocal(id = WALLPAPER_ID) )[UNKNOWN_WALLPAPER_ID].shouldBeNone()
    }
}

private const val UPDATED_DOWNLOADS = 9999
private const val WALLPAPER_ID = "1"
private const val UNKNOWN_WALLPAPER_ID = "2"

