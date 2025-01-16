package com.sefford.artdrian.downloads.domain.model

import com.sefford.artdrian.common.language.units.Size.Companion.bytes
import io.kotest.matchers.longs.shouldBeZero
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldBeEmpty
import org.junit.Test

class DownloadDtoCreationTest {

    @Test
    fun `creates pending`() {
        Download.Pending(ID, IMAGE).toDto().should { dto ->
            dto.id shouldBe ID
            dto.url shouldBe IMAGE
            dto.hash.shouldBeEmpty()
            dto.total shouldBe INVALID
            dto.downloaded shouldBe INVALID
            dto.uri.shouldBeEmpty()
        }
    }

    @Test
    fun `creates primed`() {
        Download.Primed(ID, IMAGE, HASH, TOTAL).toDto().should { dto ->
            dto.id shouldBe ID
            dto.url shouldBe IMAGE
            dto.hash shouldBe HASH
            dto.total shouldBe TOTAL.inBytes
            dto.downloaded.shouldBeZero()
            dto.uri.shouldBeEmpty()
        }
    }

    @Test
    fun `creates ongoing`() {
        Download.Ongoing(ID, IMAGE, HASH, TOTAL, PROGRESS, URI).toDto().should { dto ->
            dto.id shouldBe ID
            dto.url shouldBe IMAGE
            dto.hash shouldBe HASH
            dto.total shouldBe TOTAL.inBytes
            dto.downloaded shouldBe PROGRESS.inBytes
            dto.uri shouldBe URI
        }
    }

    @Test
    fun `creates finished`() {
        Download.Finished(ID, IMAGE, HASH, TOTAL, URI).toDto().should { dto ->
            dto.id shouldBe ID
            dto.url shouldBe IMAGE
            dto.hash shouldBe HASH
            dto.total shouldBe TOTAL.inBytes
            dto.downloaded shouldBe TOTAL.inBytes
            dto.uri shouldBe URI
        }
    }
}

private const val ID = "pending"
private const val HASH = "1234"
private const val IMAGE = "http://example.com/image.jpg"
private val TOTAL = 1000L.bytes
private val PROGRESS = 250L.bytes
private const val URI = "file://target/1234"
private const val INVALID = -1L
