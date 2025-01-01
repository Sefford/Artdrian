package com.sefford.artdrian.wallpapers.store

import com.sefford.artdrian.data.DataError
import com.sefford.artdrian.model.WallpaperList
import com.sefford.artdrian.test.mothers.WallpaperMother
import io.kotest.assertions.arrow.core.shouldBeNone
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.jupiter.api.Test

class ErrorWallpaperStateTest {

    private val error = WallpapersState.Error(DataError.Network.NotFound("1234"))

    @Test
    fun `merges with a list of wallpapers`() {
        (error + WallpaperMother.generateLocal().toList()).should { newState ->
            newState.shouldBeInstanceOf<WallpapersState.Loaded>()
            newState.wallpapers.first().source.local.shouldBeTrue()
            newState.source.local.shouldBeTrue()
            newState.next shouldBe WallpapersState.Idle
        }
    }

    @Test
    fun `merges with a single wallpaper`() {
        (error + WallpaperMother.generateLocal().toList()).should { newState ->
            newState.shouldBeInstanceOf<WallpapersState.Loaded>()
            newState.wallpapers.first().source.local.shouldBeTrue()
            newState.source.local.shouldBeTrue()
            newState.next shouldBe WallpapersState.Idle
        }
    }

    @Test
    fun `discards merging with an error from cache`() {
        (error + DataError.Local.NotFound("1234")).should { newState ->
            newState.shouldBeInstanceOf<WallpapersState.Error>()
            newState.error.shouldBeInstanceOf<DataError.Network.NotFound>()
        }
    }

    @Test
    fun `updates error from same type`() {
        (error + DataError.Network.Invalid(400)).should { newState ->
            newState.shouldBeInstanceOf<WallpapersState.Error>()
            newState.error.shouldBeInstanceOf<DataError.Network.Invalid>()
        }
    }

    @Test
    fun `overrides a cache error`() {
        (WallpapersState.Error(
            DataError.Local.NotFound("1234"),
        ) + DataError.Network.Invalid(400)).should { newState ->
            newState.shouldBeInstanceOf<WallpapersState.Error>()
            newState.error.shouldBeInstanceOf<DataError.Network.Invalid>()
        }
    }

    @Test
    fun `transient items are empty`() {
        (WallpapersState.Error(DataError.Local.NotFound("1234"))).transient.shouldBeEmpty()
    }

    @Test
    fun `returns no wallpaper`() {
        (WallpapersState.Error(DataError.Local.NotFound("1234")))["1"].shouldBeNone()
    }
}
