package com.sefford.artdrian.downloads.store

import com.sefford.artdrian.common.data.DataError
import com.sefford.artdrian.common.language.files.Size.Companion.bytes
import com.sefford.artdrian.common.language.files.writeString
import com.sefford.artdrian.downloads.domain.model.Download
import com.sefford.artdrian.test.mothers.DownloadsMother
import io.kotest.matchers.collections.shouldContainOnly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.jupiter.api.Test
import java.nio.file.Files

class LoadedDownloadsStateTest {

    @Test
    fun `Loaded plus Error equals Loaded`() {
        (DownloadsState.Loaded(setOf(DownloadsMother.createPending())) + ERROR).should { loaded ->
            loaded.shouldBeInstanceOf<DownloadsState.Loaded>()
            loaded.downloads.shouldHaveSize(1)
        }
    }

    @Test
    fun `Loaded plus Empty Preload equals Loaded`() {
        (DownloadsState.Loaded(setOf(DownloadsMother.createPending())) + DownloadsState.Preload(emptySet())).should { loaded ->
            loaded.shouldBeInstanceOf<DownloadsState.Loaded>()
            loaded.downloads.shouldHaveSize(1)
        }
    }

    @Test
    fun `Loaded plus Preload equals Loaded`() {
        (DownloadsState.Loaded(setOf(DownloadsMother.createPending())) + DownloadsState.Preload(
            setOf(DownloadsMother.createPending(SECOND_URL ))
        )).should { loaded ->
            loaded.shouldBeInstanceOf<DownloadsState.Loaded>()
            loaded.downloads.shouldContainOnly(DownloadsMother.createPending())
        }
    }

    @Test
    fun `returns pending downloads`() {
        DownloadsState.Loaded(
            setOf(
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
            setOf(
                DownloadsMother.createPending(),
                DownloadsMother.createOngoing(url = SECOND_URL),
                DownloadsMother.createFinished(url = THIRD_URL)
            )
        ).total shouldBe TOTAL_DOWNLOAD_SIZE
    }

    @Test
    fun `returns total progress size`() {
        val ongoing = Files.createTempFile("ongoing", ".download").toFile()
        ongoing.writeString("a".repeat(ONGOING_FILE_PROGRESS.inBytes.toInt()))
        val finishedFile = Files.createTempFile("finished", ".download").toFile()
        finishedFile.writeString("a".repeat(FINISHED_FILE_PROGRESS.inBytes.toInt()))

        DownloadsState.Loaded(
            setOf(
                DownloadsMother.createPending(),
                DownloadsMother.createOngoing(url = SECOND_URL, file = ongoing),
                DownloadsMother.createFinished(url = THIRD_URL, file = finishedFile)
            )
        ).progress shouldBe TOTAL_PROGRESS_SIZE
    }
}

private val ERROR = DataError.Local.Critical(RuntimeException())
private val TOTAL_DOWNLOAD_SIZE = 2000L.bytes
private val ONGOING_FILE_PROGRESS = 250.bytes
private val FINISHED_FILE_PROGRESS = 1000.bytes
private val TOTAL_PROGRESS_SIZE = ONGOING_FILE_PROGRESS + FINISHED_FILE_PROGRESS
private val SECOND_URL = "http://example.com/2/image.jpg"
private val THIRD_URL = "http://example.com/3/image.jpg"



