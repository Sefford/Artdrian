package com.sefford.artdrian.wallpapers.domain.usecases

import com.karumi.kotlinsnapshot.matchWithSnapshot
import com.sefford.artdrian.common.FakeFileManager
import com.sefford.artdrian.wallpapers.domain.usecases.DownloadWallpaper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DownloadWallpaperTestBuilder {

    private lateinit var useCase: DownloadWallpaper

    @Test
    fun `returns an URI when the file is successfully saved`() = runTest {
        useCase = DownloadWallpaper(FakeFileManager { ANY_SAVED_WALLPAPER_URI })

        useCase.download(ANY_WALLPAPER_URL).matchWithSnapshot()
    }

    @Test
    fun `returns an PersistenceError with the underlying exception`() = runTest {
        useCase = DownloadWallpaper(FakeFileManager { throw IllegalStateException("This was an error") })

        useCase.download(ANY_WALLPAPER_URL).matchWithSnapshot()
    }

    private companion object {
        const val ANY_WALLPAPER_URL = "http://test.jpg"
        const val ANY_SAVED_WALLPAPER_URI = "content://images/1234"
    }
}
