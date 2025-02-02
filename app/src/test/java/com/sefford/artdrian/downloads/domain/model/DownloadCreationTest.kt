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
            download.url shouldBe IMAGE
        }
    }

    @Test
    fun `creates in progress`() {
        val downloadFile = Files.createTempFile(DownloadsMother.createOngoing().fileName, "").toFile()
        downloadFile.writeString("a".repeat(PROGRESS.inBytes.toInt()))

        DownloadsDtoMother.createOngoing(uri = downloadFile.absolutePath).toDomain().should { download ->
            download.shouldBeInstanceOf<Download.Ongoing>()
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
            download.url shouldBe IMAGE
            download.hash shouldBe HASH
            download.total shouldBe TOTAL
            download.progress shouldBe TOTAL
        }
    }
}

private const val HASH = "1234"
private const val IMAGE = "http://example.com/mobile/image.jpg"
private val TOTAL = 1000L.bytes
private val PROGRESS = 250L.bytes
