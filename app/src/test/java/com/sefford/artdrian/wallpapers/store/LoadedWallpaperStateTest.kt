package com.sefford.artdrian.wallpapers.store

import com.sefford.artdrian.data.DataError
import com.sefford.artdrian.model.Source
import com.sefford.artdrian.model.Wallpaper
import com.sefford.artdrian.model.WallpaperList
import com.sefford.artdrian.test.mothers.MetadataMother
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
        val state = WallpapersState.Loaded(emptyList(), Source.LOCAL, WallpapersState.Idle)

        (state + WallpaperList(MetadataMother.generate(), source = Source.LOCAL)).should { newState ->
            newState.shouldBeInstanceOf<WallpapersState.Loaded>()
            newState.wallpapers.first().source.local.shouldBeTrue()
            newState.source.local.shouldBeTrue()
            newState.next shouldBe WallpapersState.Idle
        }
    }

    @Test
    fun `merges with an empty state with a network response`() {
        val state = WallpapersState.Loaded(emptyList(), Source.LOCAL, WallpapersState.Idle)

        (state + WallpaperList(MetadataMother.generate(), source = Source.NETWORK)).should { newState ->
            newState.shouldBeInstanceOf<WallpapersState.Loaded>()
            newState.wallpapers.first().source.network.shouldBeTrue()
            newState.source.network.shouldBeTrue()
            newState.next shouldBe WallpapersState.Idle
        }
    }

    @Test
    fun `overrides a local metadata with a network metadata`() {
        val state = WallpapersState.Loaded(
            listOf(Wallpaper(MetadataMother.generate(), Source.LOCAL)),
            Source.LOCAL,
            WallpapersState.Idle
        )

        (state + WallpaperList(MetadataMother.generate(), source = Source.NETWORK)).should { newState ->
            newState.shouldBeInstanceOf<WallpapersState.Loaded>()
            newState.wallpapers.first().source.network.shouldBeTrue()
            newState.source.network.shouldBeTrue()
            newState.next shouldBe WallpapersState.Idle
        }
    }

    @Test
    fun `overrides a network metadata with a network metadata`() {
        val state = WallpapersState.Loaded(
            listOf(Wallpaper(MetadataMother.generate(), Source.LOCAL)),
            Source.NETWORK,
            WallpapersState.Idle
        )

        (state + WallpaperList(
            MetadataMother.generate(views = UPDATED_VIEWS),
            source = Source.NETWORK
        )).should { newState ->
            newState.shouldBeInstanceOf<WallpapersState.Loaded>()
            newState.wallpapers.first().source.network.shouldBeTrue()
            newState.wallpapers.first().metadata.views shouldBe UPDATED_VIEWS
            newState.source.network.shouldBeTrue()
            newState.next shouldBe WallpapersState.Idle
        }
    }

    @Test
    fun `overrides a local metadata with a local metadata`() {
        val state = WallpapersState.Loaded(
            listOf(Wallpaper(MetadataMother.generate(), Source.LOCAL)),
            Source.LOCAL,
            WallpapersState.Idle
        )

        (state + WallpaperList(
            MetadataMother.generate(views = UPDATED_VIEWS),
            source = Source.LOCAL
        )).should { newState ->
            newState.shouldBeInstanceOf<WallpapersState.Loaded>()
            newState.wallpapers.first().source.local.shouldBeTrue()
            newState.wallpapers.first().metadata.views shouldBe UPDATED_VIEWS
            newState.source.local.shouldBeTrue()
            newState.next shouldBe WallpapersState.Idle
        }
    }

    @Test
    fun `does not a network metadata with a local metadata`() {
        val state = WallpapersState.Loaded(
            listOf(Wallpaper(MetadataMother.generate(), Source.NETWORK)),
            Source.NETWORK,
            WallpapersState.Idle
        )

        (state + WallpaperList(MetadataMother.generate(), source = Source.LOCAL)).should { newState ->
            newState.shouldBeInstanceOf<WallpapersState.Loaded>()
            newState.wallpapers.first().source.network.shouldBeTrue()
            newState.source.network.shouldBeTrue()
            newState.next shouldBe WallpapersState.Idle
        }
    }

    @Test
    fun `merges with a single element`() {
        val state = WallpapersState.Loaded(emptyList(), Source.LOCAL, WallpapersState.Idle)

        (state + Wallpaper(MetadataMother.generate(), source = Source.LOCAL)).should { newState ->
            newState.shouldBeInstanceOf<WallpapersState.Loaded>()
            newState.wallpapers.first().source.local.shouldBeTrue()
            newState.source.local.shouldBeTrue()
            newState.next shouldBe WallpapersState.Idle
        }
    }

    @Test
    fun `merges with an error of the same type`() {
        val state = WallpapersState.Loaded(
            listOf(Wallpaper(MetadataMother.generate(), source = Source.LOCAL)),
            Source.LOCAL,
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
            listOf(Wallpaper(MetadataMother.generate(), source = Source.LOCAL)),
            Source.LOCAL,
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
            listOf(Wallpaper(MetadataMother.generate(), source = Source.NETWORK)),
            Source.NETWORK,
            WallpapersState.Error(DataError.Network.Invalid(HttpStatusCode.BadRequest.value))
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
                Wallpaper(MetadataMother.generate(), source = Source.NETWORK),
                Wallpaper(MetadataMother.generate(), source = Source.LOCAL)
            ),
            Source.NETWORK,
            WallpapersState.Error(DataError.Network.Invalid(HttpStatusCode.BadRequest.value))
        ).transient.shouldHaveSize(1)
    }

    @Test
    fun `transient items are empty if the response is from Cache`() {
        WallpapersState.Loaded(
            listOf(
                Wallpaper(MetadataMother.generate(), source = Source.NETWORK),
                Wallpaper(MetadataMother.generate(), source = Source.NETWORK)
            ),
            Source.LOCAL,
            WallpapersState.Error(DataError.Network.Invalid(HttpStatusCode.BadRequest.value))
        ).transient.shouldBeEmpty()
    }

    @Test
    fun `returns a wallpaper when it is loaded`() {
        WallpapersState.Loaded(
            listOf(Wallpaper(MetadataMother.generate(id = WALLPAPER_ID), Source.LOCAL)),
            Source.LOCAL,
            WallpapersState.Idle
        )[WALLPAPER_ID].shouldBeSome()
    }

    @Test
    fun `returns a none when the wallpaper does not exist`() {
        WallpapersState.Loaded(
            listOf(Wallpaper(MetadataMother.generate(id = WALLPAPER_ID), Source.LOCAL)),
            Source.LOCAL,
            WallpapersState.Idle
        )[UNKNOWN_WALLPAPER_ID].shouldBeNone()
    }
}

private const val UPDATED_VIEWS = 9999
private const val WALLPAPER_ID = "1"
private const val UNKNOWN_WALLPAPER_ID = "2"

