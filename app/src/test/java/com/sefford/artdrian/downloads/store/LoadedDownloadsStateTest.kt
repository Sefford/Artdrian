package com.sefford.artdrian.downloads.store

import com.sefford.artdrian.common.data.DataError
import com.sefford.artdrian.downloads.domain.model.Download
import com.sefford.artdrian.test.mothers.DownloadsMother
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.jupiter.api.Test

class LoadedDownloadsStateTest {

    @Test
    fun `Loaded plus Error equals Loaded`() {
        (DownloadsState.Loaded(listOf(DownloadsMother.createPending())) + ERROR).should { loaded ->
            loaded.shouldBeInstanceOf<DownloadsState.Loaded>()
            loaded.downloads.shouldHaveSize(1)
        }
    }

    @Test
    fun `Loaded plus Empty Preload equals Loaded`() {
        (DownloadsState.Loaded(listOf(DownloadsMother.createPending())) + DownloadsState.Preload(listOf())).should { loaded ->
            loaded.shouldBeInstanceOf<DownloadsState.Loaded>()
            loaded.downloads.shouldHaveSize(1)
        }
    }

    @Test
    fun `Loaded plus Preload equals Loaded`() {
        (DownloadsState.Loaded(listOf(DownloadsMother.createPending())) + DownloadsState.Preload(
            listOf(
                DownloadsMother.createPending(
                    "2"
                )
            )
        )).should { loaded ->
            loaded.shouldBeInstanceOf<DownloadsState.Loaded>()
            loaded.downloads.shouldHaveSize(2)
        }
    }

    @Test
    fun `Loaded plus empty Downloads equals Loaded`() {
        (DownloadsState.Loaded(listOf(DownloadsMother.createPending())) + listOf()).should { loaded ->
            loaded.shouldBeInstanceOf<DownloadsState.Loaded>()
            loaded.downloads.shouldHaveSize(1)
        }
    }

    @Test
    fun `Loaded plus Downloads equals Loaded`() {
        (DownloadsState.Loaded(listOf(DownloadsMother.createPending())) + listOf(DownloadsMother.createPending("2"))).should { loaded ->
            loaded.shouldBeInstanceOf<DownloadsState.Loaded>()
            loaded.downloads.shouldHaveSize(2)
        }
    }


    @Test
    fun `returns pending downloads`() {
        DownloadsState.Loaded(
            listOf(
                DownloadsMother.createPending(),
                DownloadsMother.createFinished()
            )
        ).pending.should { pending ->
            pending.shouldHaveSize(1)
            pending.first().shouldBeInstanceOf<Download.Pending>()
        }
    }

    @Test
    fun `returns total download size`() {
        DownloadsState.Loaded(
            listOf(
                DownloadsMother.createPending(),
                DownloadsMother.createPrimed(),
                DownloadsMother.createOngoing(),
                DownloadsMother.createFinished()
            )
        ).total shouldBe TOTAL_DOWNLOAD_SIZE
    }

    @Test
    fun `returns total progress size`() {
        DownloadsState.Loaded(
            listOf(
                DownloadsMother.createPending(),
                DownloadsMother.createPrimed(),
                DownloadsMother.createOngoing(),
                DownloadsMother.createFinished()
            )
        ).progress shouldBe TOTAL_PROGRESS_SIZE
    }

}

private val ERROR = DataError.Local.Critical(RuntimeException())
private val TOTAL_DOWNLOAD_SIZE = 3000
private val TOTAL_PROGRESS_SIZE = 1250

