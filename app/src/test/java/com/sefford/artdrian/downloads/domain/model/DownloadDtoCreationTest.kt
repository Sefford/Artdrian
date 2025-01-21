package com.sefford.artdrian.downloads.domain.model

import com.sefford.artdrian.common.language.files.Size.Companion.bytes
import com.sefford.artdrian.common.language.files.writeString
import com.sefford.artdrian.test.mothers.DownloadsMother
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldBeEmpty
import org.junit.Test
import java.io.File
import java.nio.file.Files

class DownloadDtoCreationTest {

    @Test
    fun `creates pending`() {

        Download.Pending(ID, IMAGE).toDto().should { dto ->
            dto.id shouldBe ID
            dto.url shouldBe IMAGE
            dto.hash.shouldBeEmpty()
            dto.name.shouldBeEmpty()
            dto.total shouldBe INVALID
            dto.downloaded shouldBe INVALID
            dto.uri.shouldBeEmpty()
        }
    }

    @Test
    fun `creates ongoing`() {
        val downloadFile = Files.createTempFile(DownloadsMother.createOngoing().name, ".download").toFile()
        downloadFile.writeString("a".repeat(PROGRESS.inBytes.toInt()))

        Download.Ongoing(ID, IMAGE, HASH, NAME, TOTAL, downloadFile).toDto().should { dto ->
            dto.id shouldBe ID
            dto.url shouldBe IMAGE
            dto.hash shouldBe HASH
            dto.name shouldBe NAME
            dto.total shouldBe TOTAL.inBytes
            dto.downloaded shouldBe PROGRESS.inBytes
            dto.uri shouldBe downloadFile.absolutePath
        }
    }

    @Test
    fun `creates finished`() {
        val downloadFile = Files.createTempFile(DownloadsMother.createOngoing().name, ".download").toFile()

        Download.Finished(ID, IMAGE, HASH, NAME, TOTAL, downloadFile).toDto().should { dto ->
            dto.id shouldBe ID
            dto.url shouldBe IMAGE
            dto.hash shouldBe HASH
            dto.name shouldBe NAME
            dto.total shouldBe TOTAL.inBytes
            dto.downloaded shouldBe TOTAL.inBytes
            dto.uri shouldBe downloadFile.absolutePath
        }
    }
}

private const val ID = "pending"
private const val HASH = "1234"
private const val IMAGE = "http://example.com/image.jpg"
private const val NAME = "ghost_waves.jpg"
private val TOTAL = 1000L.bytes
private val PROGRESS = 250L.bytes
private const val INVALID = -1L
