package com.sefford.artdrian.usecases

import com.karumi.kotlinsnapshot.matchWithSnapshot
import com.sefford.artdrian.common.FakeFileManager
import com.sefford.artdrian.common.FakeWallpaperAdapter
import com.sefford.artdrian.common.WallpaperAdapter
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class SetWallpaperTest {

    private lateinit var useCase: SetWallpaper

    @Test
    fun `returns successfully after applying the wallpaper`() {
        useCase = SetWallpaper(FakeWallpaperAdapter())

        runBlocking {
            useCase.setWallpaper(ANY_WALLPAPER_URL).matchWithSnapshot()
        }
    }

    @Test
    fun `returns the underlying exception`() {
        useCase = SetWallpaper(FakeWallpaperAdapter{ throw IllegalStateException("This was an error") })

        runBlocking {
            useCase.setWallpaper(ANY_WALLPAPER_URL).matchWithSnapshot()
        }
    }

    private companion object {
        const val ANY_WALLPAPER_URL = "http://test.jpg"
    }
}
