package com.sefford.artdrian.downloads.store

import com.sefford.artdrian.common.data.DataError
import com.sefford.artdrian.test.mothers.WallpaperMother
import com.sefford.artdrian.test.unconfine
import com.sefford.artdrian.wallpapers.store.WallpapersState
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class WallpaperPredownloadBridgeTest {

    @Test
    fun `links correctly`() = runTest {
        val events: (DownloadsEvents) -> Unit = { event: DownloadsEvents ->
            event.shouldBeInstanceOf<DownloadsEvents.Register>()
            event.downloads.shouldHaveSize(2)
            event.downloads.first().url shouldBe "https://example.com/mobile.jpg"
            event.downloads.last().url shouldBe "https://example.com/desktop.jpg"
        }

        MutableStateFlow(
            WallpapersState.Loaded(WallpaperMother.generateNetwork())
        ).bridgeToDownload(events, backgroundScope.unconfine())
    }

    @Test
    fun `filters correctly`() = runTest {
        val events: (DownloadsEvents) -> Unit = { event: DownloadsEvents ->
            throw IllegalArgumentException("This event should never happen")
        }

        val state = MutableStateFlow<WallpapersState>(WallpapersState.Idle.Empty)

        state.bridgeToDownload(events, backgroundScope.unconfine())
        state.update { WallpapersState.Idle.OnLocalError(DataError.Local.Empty) }
        state.update { WallpapersState.Idle.OnNetworkError(DataError.Network.NoConnection) }
        state.update { WallpapersState.Error(DataError.Network.NoConnection) }
    }
}
