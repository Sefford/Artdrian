package com.sefford.artdrian.downloads.domain.model

import com.sefford.artdrian.common.language.files.contentToString
import com.sefford.artdrian.common.language.files.writeString
import com.sefford.artdrian.downloads.store.DownloadsEvents
import com.sefford.artdrian.test.networking.FakeByteReadChannel
import com.sefford.artdrian.test.InjectableTest
import com.sefford.artdrian.test.networking.LazyMockEngineHandler
import com.sefford.artdrian.test.mothers.DownloadsMother
import io.kotest.matchers.shouldBe
import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.channels.UnresolvedAddressException
import java.nio.file.Files
import javax.inject.Inject

class DownloadProcessFetchTest : InjectableTest() {

    private val cache = Files.createTempDirectory("downloads").toFile()

    @Inject
    internal lateinit var client: HttpClient

    @Inject
    internal lateinit var handlers: LazyMockEngineHandler

    @BeforeEach
    override fun beforeEach() {
        super.beforeEach()
        graph.inject(this)
    }

    @Test
    fun `downloads a file`() = runTest {
        val downloadFile = File(cache, "${DownloadsMother.createOngoing().name}.jpg.download")

        givenAFetch(download = DownloadsMother.createOngoing(file = downloadFile)) shouldBe DownloadProcess.Results.Success

        downloadFile.contentToString() shouldBe CONTENT
    }

    @Test
    fun `downloads a partial file`() = runTest {
        val downloadFile = File.createTempFile(DownloadsMother.createOngoing().name, ".jpg").also { file ->
            file.writeString(PARTIAL_DOWNLOADED_CONTENT)
        }

        givenAFetch(
            status = HttpStatusCode.PartialContent,
            source = ByteReadChannel(PARTIAL_CONTENT_TO_DOWNLOAD),
            download = DownloadsMother.createOngoing(file = downloadFile)
        ) shouldBe DownloadProcess.Results.Success

        downloadFile.contentToString() shouldBe CONTENT
    }

    @Test
    fun `returns failure when received an unexpected exception`() = runTest {
        givenAFetch(
            source = FakeByteReadChannel(onReadSource = { throw IllegalStateException() }),
            download = DownloadsMother.createOngoing()
        ) shouldBe DownloadProcess.Results.Failure
    }

    @Test
    fun `returns retry when receiving a connectivity problem`() = runTest {
        givenAFetch(
            source = FakeByteReadChannel(onReadSource = { throw UnresolvedAddressException() }),
            download = DownloadsMother.createOngoing()
        ) shouldBe DownloadProcess.Results.Retry
    }

    private suspend fun givenAFetch(
        events: (DownloadsEvents) -> Unit = {},
        status: HttpStatusCode = HttpStatusCode.OK,
        source: ByteReadChannel = ByteReadChannel(CONTENT),
        download: Download.Ongoing = DownloadsMother.createOngoing()
    ) = DownloadProcess.Step.Fetch(
        events = events,
        status = status,
        body = source,
        download = download
    ).start()
}

private const val CONTENT = "This is really a wallpaper, promise!"
private const val PARTIAL_DOWNLOADED_CONTENT = "This is really "
private const val PARTIAL_CONTENT_TO_DOWNLOAD = "a wallpaper, promise!"
