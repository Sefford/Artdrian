package com.sefford.artdrian.downloads.store

import com.sefford.artdrian.common.data.DataError
import com.sefford.artdrian.test.mothers.DownloadsMother
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainOnly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.should
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.jupiter.api.Test

class PreloadDowloadsStateTest {

    @Test
    fun `Preload plus Error equals Loaded`() {
        (DownloadsState.Preload(listOf(DownloadsMother.createPending())) + ERROR).should { loaded ->
            loaded.shouldBeInstanceOf<DownloadsState.Loaded>()
            loaded.downloads.shouldHaveSize(1)
        }
    }

    @Test
    fun `Preload plus Empty Preload equals Preload`() {
        (DownloadsState.Preload(listOf(DownloadsMother.createPending())) + DownloadsState.Preload(listOf())).should { preload ->
            preload.shouldBeInstanceOf<DownloadsState.Preload>()
            preload.downloads.shouldHaveSize(1)
        }
    }

    @Test
    fun `Preload plus Preload equals Preload`() {
        (DownloadsState.Preload(listOf(DownloadsMother.createPending())) + DownloadsState.Preload(
            listOf(
                DownloadsMother.createPending(
                    "2"
                )
            )
        )).should { preload ->
            preload.shouldBeInstanceOf<DownloadsState.Preload>()
            preload.downloads.shouldHaveSize(2)
        }
    }

    @Test
    fun `the difference between a Preload and a Loaded does not include downloads in Loaded`() {
        val pending = DownloadsMother.createPending()

        (DownloadsState.Preload(listOf(pending)) - DownloadsState.Loaded(listOf(DownloadsMother.createOngoing(url = OTHER_DOWNLOAD_URL))))
            .shouldContainOnly(pending)
    }
}

private val ERROR = DataError.Local.Critical(RuntimeException())
private val OTHER_DOWNLOAD_URL = "http://example.com/desktop/image.jpg"
