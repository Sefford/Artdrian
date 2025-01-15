package com.sefford.artdrian.downloads.domain.model

import com.sefford.artdrian.test.mothers.DownloadsMother
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.longs.shouldBeZero
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.Test

class DownloadTest {

    @Test
    fun primes() {
        Download.Pending(ID, IMAGE).prime(HASH, TOTAL).should { download ->
            download.id shouldBe ID
            download.url shouldBe IMAGE
            download.hash shouldBe HASH
            download.total shouldBe TOTAL
            download.progress.shouldBeZero()
        }
    }

    @Test
    fun starts() {
        Download.Pending(ID, IMAGE).prime(HASH, TOTAL).start(URI).should { download ->
            download.shouldBeInstanceOf<Download.Ongoing>()
            download.id shouldBe ID
            download.url shouldBe IMAGE
            download.hash shouldBe HASH
            download.total shouldBe TOTAL
            download.progress.shouldBeZero()
            download.uri shouldBe URI
        }
    }

    @Test
    fun appends() {
        (Download.Pending(ID, IMAGE).prime(HASH, TOTAL).start(URI) + PROGRESS).should { download ->
            download.shouldBeInstanceOf<Download.Ongoing>()
            download.id shouldBe ID
            download.url shouldBe IMAGE
            download.hash shouldBe HASH
            download.total shouldBe TOTAL
            download.progress shouldBe PROGRESS
            download.uri shouldBe URI
        }
    }

    @Test
    fun finishes() {
        (Download.Pending(ID, IMAGE).prime(HASH, TOTAL).start(URI) + TOTAL).finish().should { download ->
            download.shouldBeInstanceOf<Download.Finished>()
            download.id shouldBe ID
            download.url shouldBe IMAGE
            download.hash shouldBe HASH
            download.total shouldBe TOTAL
            download.progress shouldBe TOTAL
            download.uri shouldBe URI
        }
    }

    @Test
    fun `fails if the file is not completely downloaded`() {
        shouldThrow<IllegalStateException> { Download.Pending(ID, IMAGE).prime(HASH, TOTAL).start(URI).finish() }
    }

    @Test
    fun `Primed returns total size`() {
        DownloadsMother.createPrimed().total shouldBe TOTAL
    }

    @Test
    fun `Primed progress is zero`() {
        DownloadsMother.createPrimed().progress.shouldBeZero()
    }

    @Test
    fun `Ongoing returns total size`() {
        DownloadsMother.createOngoing().total shouldBe TOTAL
    }

    @Test
    fun `Ongoing returns progress`() {
        DownloadsMother.createOngoing().progress shouldBe PROGRESS
    }

    @Test
    fun `Finished returns total size`() {
        DownloadsMother.createFinished().total shouldBe TOTAL
    }

    @Test
    fun `Finished returns progress`() {
        DownloadsMother.createFinished().progress shouldBe TOTAL
    }
}

private const val ID = "pending"
private const val HASH = "1234"
private const val IMAGE = "http://example.com/image.jpg"
private const val TOTAL = 1000L
private const val PROGRESS = 250L
private const val URI = "file://target/1234"
private const val INVALID = -1L
