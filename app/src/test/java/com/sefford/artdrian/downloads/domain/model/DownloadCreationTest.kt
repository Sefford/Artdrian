package com.sefford.artdrian.downloads.domain.model

import com.sefford.artdrian.common.language.files.Size.Companion.bytes
import com.sefford.artdrian.common.language.files.writeString
import com.sefford.artdrian.test.mothers.DownloadsDtoMother
import com.sefford.artdrian.test.mothers.DownloadsMother
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.Test
import java.nio.file.Files

class DownloadCreationTest {

    @Test
    fun `creates pending`() {
        DownloadsDtoMother.createPending().toDomain().should { download ->
            download.shouldBeInstanceOf<Download.Pending>()
            download.id shouldBe PENDING_ID
            download.url shouldBe IMAGE
        }
    }

    @Test
    fun `creates in progress`() {
        val downloadFile = Files.createTempFile(DownloadsMother.createOngoing().name, ".download").toFile()
        downloadFile.writeString("a".repeat(PROGRESS.inBytes.toInt()))

        DownloadsDtoMother.createOngoing(uri = downloadFile.absolutePath).toDomain().should { download ->
            download.shouldBeInstanceOf<Download.Ongoing>()
            download.id shouldBe ONGOING_ID
            download.url shouldBe IMAGE
            download.hash shouldBe HASH
            download.total shouldBe TOTAL
            download.progress shouldBe PROGRESS
        }
    }

    @Test
    fun `creates finished`() {
        DownloadsDtoMother.createFinished().toDomain().should { download ->
            download.shouldBeInstanceOf<Download.Finished>()
            download.id shouldBe FINISHED_ID
            download.url shouldBe IMAGE
            download.hash shouldBe HASH
            download.total shouldBe TOTAL
            download.progress shouldBe TOTAL
        }
    }

}

private const val PENDING_ID = "pending"
private const val ONGOING_ID = "ongoing"
private const val FINISHED_ID = "finished"
private const val HASH = "1234"
private const val IMAGE = "http://example.com/image.jpg"
private val TOTAL = 1000L.bytes
private val PROGRESS = 250L.bytes
