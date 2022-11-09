package com.sefford.artdrian.usecases

import com.karumi.kotlinsnapshot.matchWithSnapshot
import com.sefford.artdrian.common.FakeFileManager
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class DownloadWallpaperTest {

    private lateinit var useCase: DownloadWallpaper

    @Test
    fun `returns an URI when the file is successfully saved`() {
        useCase = DownloadWallpaper(FakeFileManager{ ANY_SAVED_WALLPAPER_URI })

        runBlocking {
            useCase.download(ANY_WALLPAPER_URL).matchWithSnapshot()
        }
    }

    @Test
    fun `returns an PersistenceError with the underlying exception`() {
        useCase = DownloadWallpaper(FakeFileManager{ throw IllegalStateException("This was an error") })

        runBlocking {
            useCase.download(ANY_WALLPAPER_URL).matchWithSnapshot()
        }
    }

    private companion object {
        const val ANY_WALLPAPER_URL = "http://test.jpg"
        const val ANY_SAVED_WALLPAPER_URI = "content://images/1234"
    }
}
