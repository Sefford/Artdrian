package com.sefford.artdrian.downloads.domain.model

import com.sefford.artdrian.test.mothers.DownloadsDtoMother
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.Test

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
    fun `creates primed`() {
        DownloadsDtoMother.createPrimed().toDomain().should { download ->
            download.shouldBeInstanceOf<Download.Primed>()
            download.id shouldBe PRIMED_ID
            download.url shouldBe IMAGE
            download.hash shouldBe HASH
            download.total shouldBe TOTAL
            download.progress shouldBe 0
        }
    }
    @Test
    fun `creates in progress`() {
        DownloadsDtoMother.createOngoing().toDomain().should { download ->
            download.shouldBeInstanceOf<Download.InProgress>()
            download.id shouldBe ONGOING_ID
            download.url shouldBe IMAGE
            download.hash shouldBe HASH
            download.total shouldBe TOTAL
            download.progress shouldBe PROGRESS
            download.uri shouldBe URI
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
            download.uri shouldBe URI
        }
    }

}

private const val PENDING_ID = "pending"
private const val PRIMED_ID = "primed"
private const val ONGOING_ID = "ongoing"
private const val FINISHED_ID = "finished"
private const val HASH = "1234"
private const val IMAGE = "http://example.com/image.jpg"
private const val TOTAL = 1000L
private const val PROGRESS = 250L
private const val URI = "file://target/1234"
