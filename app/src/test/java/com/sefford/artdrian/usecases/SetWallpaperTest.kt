package com.sefford.artdrian.usecases

import com.karumi.kotlinsnapshot.matchWithSnapshot
import com.sefford.artdrian.common.FakeWallpaperAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SetWallpaperTest {

    private lateinit var useCase: SetWallpaper

    @Test
    fun `returns successfully after applying the wallpaper`() = runTest {
        useCase = SetWallpaper(FakeWallpaperAdapter())

        useCase.setWallpaper(ANY_WALLPAPER_URL).matchWithSnapshot()
    }

    @Test
    fun `returns the underlying exception`() = runTest {
        useCase = SetWallpaper(FakeWallpaperAdapter { throw IllegalStateException("This was an error") })

        useCase.setWallpaper(ANY_WALLPAPER_URL).matchWithSnapshot()
    }

    private companion object {
        const val ANY_WALLPAPER_URL = "http://test.jpg"
    }
}
