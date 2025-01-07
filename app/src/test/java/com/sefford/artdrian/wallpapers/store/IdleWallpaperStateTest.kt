package com.sefford.artdrian.wallpapers.store

import com.sefford.artdrian.data.DataError
import com.sefford.artdrian.test.mothers.WallpaperMother
import io.kotest.assertions.arrow.core.shouldBeNone
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.jupiter.api.Test

class IdleWallpaperStateTest {

    @Test
    fun `merges with a list of wallpapers`() {
        (WallpapersState.Idle.Empty + WallpaperMother.generateLocal().toList()).should { newState ->
            newState.shouldBeInstanceOf<WallpapersState.Loaded>()
            newState.wallpapers.first().source.local.shouldBeTrue()
            newState.source.local.shouldBeTrue()
            newState.next shouldBe WallpapersState.Idle.Empty
        }
    }

    @Test
    fun `merges with a single wallpaper`() {
        (WallpapersState.Idle.Empty + WallpaperMother.generateLocal().toList()).should { newState ->
            newState.shouldBeInstanceOf<WallpapersState.Loaded>()
            newState.wallpapers.first().source.local.shouldBeTrue()
            newState.source.local.shouldBeTrue()
            newState.next shouldBe WallpapersState.Idle.Empty
        }
    }

    @Test
    fun `holds a network error`() {
        (WallpapersState.Idle.Empty + DataError.Network.NoConnection).should { newState ->
            newState.shouldBeInstanceOf<WallpapersState.Idle.OnNetworkError>()
            newState.error.shouldBeInstanceOf<DataError.Network.NoConnection>()
        }
    }

    @Test
    fun `holds a cache error`() {
        (WallpapersState.Idle.Empty + DataError.Local.Empty).should { newState ->
            newState.shouldBeInstanceOf<WallpapersState.Idle.OnLocalError>()
            newState.error.shouldBeInstanceOf<DataError.Local.Empty>()
        }
    }

    @Test
    fun `overwrites a network error on idle when I receive a secondary error`() {
        (WallpapersState.Idle.OnNetworkError(DataError.Network.NoConnection) + DataError.Network.SocketTimeout).should { newState ->
            newState.shouldBeInstanceOf<WallpapersState.Idle.OnNetworkError>()
            newState.error.shouldBeInstanceOf<DataError.Network.SocketTimeout>()
        }
    }

    @Test
    fun `overwrites a cache error on idle when I receive a secondary error`() {
        (WallpapersState.Idle.OnLocalError(DataError.Local.Empty) + DataError.Local.Critical(NullPointerException())).should { newState ->
            newState.shouldBeInstanceOf<WallpapersState.Idle.OnLocalError>()
            newState.error.shouldBeInstanceOf<DataError.Local.Critical>()
        }
    }

    @Test
    fun `progresses to an error from network error`() {
        (WallpapersState.Idle.OnNetworkError(DataError.Network.NoConnection) + DataError.Local.Empty).should { newState ->
            newState.shouldBeInstanceOf<WallpapersState.Error>()
            newState.error.shouldBeInstanceOf<DataError.Network.NoConnection>()
        }
    }

    @Test
    fun `progresses to an error from cache error`() {
        (WallpapersState.Idle.OnLocalError(DataError.Local.Empty) + DataError.Network.NoConnection).should { newState ->
            newState.shouldBeInstanceOf<WallpapersState.Error>()
            newState.error.shouldBeInstanceOf<DataError.Network.NoConnection>()
        }
    }

    @Test
    fun `returns no wallpaper`() {
        (WallpapersState.Error(DataError.Local.NotFound("1234")))["1"].shouldBeNone()
    }
}
