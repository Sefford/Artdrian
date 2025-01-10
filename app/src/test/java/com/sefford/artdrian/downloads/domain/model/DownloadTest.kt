package com.sefford.artdrian.downloads.domain.model

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
            download.shouldBeInstanceOf<Download.InProgress>()
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
            download.shouldBeInstanceOf<Download.InProgress>()
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

}

private const val ID = "pending"
private const val HASH = "1234"
private const val IMAGE = "http://example.com/image.jpg"
private const val TOTAL = 1000L
private const val PROGRESS = 250L
private const val URI = "file://target/1234"
private const val INVALID = -1L
