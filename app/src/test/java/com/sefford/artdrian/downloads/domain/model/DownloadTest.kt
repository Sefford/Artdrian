package com.sefford.artdrian.downloads.domain.model

import com.sefford.artdrian.common.language.files.Size.Companion.bytes
import com.sefford.artdrian.common.language.files.writeString
import com.sefford.artdrian.test.assertions.shouldBeZero
import com.sefford.artdrian.test.mothers.DownloadsMother
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.ktor.http.HttpHeaders
import io.ktor.http.headers
import org.junit.Test
import java.nio.file.Files

class DownloadTest {

    private val cache = Files.createTempDirectory("downloads").toFile()

    @Test
    fun starts() {
        Download.Pending(ID, IMAGE).start(HEADERS_RESPONSE, cache).should { download ->
            download.shouldBeInstanceOf<Download.Ongoing>()
            download.id shouldBe ID
            download.url shouldBe IMAGE
            download.hash shouldBe HASH
            download.total shouldBe TOTAL
            download.progress.shouldBeZero()
        }
    }

    @Test
    fun finishes() {
        val file = Files.createFile(cache.toPath().resolve("${DownloadsMother.createOngoing().name}.download")).toFile()
        file.writeString("a".repeat(TOTAL.inBytes.toInt()))

        Download.Pending(ID, IMAGE).start(HEADERS_RESPONSE, cache).finish().should { download ->
            download.shouldBeInstanceOf<Download.Finished>()
            download.id shouldBe ID
            download.url shouldBe IMAGE
            download.hash shouldBe HASH
            download.total shouldBe TOTAL
            download.progress shouldBe TOTAL
        }
    }

    @Test
    fun `fails if the file is not completely downloaded`() {
        shouldThrow<IllegalStateException> { Download.Pending(ID, IMAGE).start(HEADERS_RESPONSE, cache).finish() }
    }

    @Test
    fun `Ongoing returns total size`() {
        DownloadsMother.createOngoing().total shouldBe TOTAL
    }

    @Test
    fun `Ongoing returns progress`() {
        val file = Files.createTempFile(DownloadsMother.createOngoing().name, ".download").toFile()
        file.writeString("a".repeat(PROGRESS.inBytes.toInt()))

        DownloadsMother.createOngoing(file = file).progress shouldBe PROGRESS
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
private const val HASH = "9cc769f284bba4616668623ca2c22f3e"
private const val IMAGE = "http://example.com/image.jpg"
private val TOTAL = 1000L.bytes
private val PROGRESS = 250L.bytes
private val HEADERS_RESPONSE = headers {
    append(HttpHeaders.ETag, HASH)
    append(HttpHeaders.ContentDisposition, "inline; filename=\"ghost_waves_004.jpg\"")
    append(HttpHeaders.ContentLength, "1000")
}
