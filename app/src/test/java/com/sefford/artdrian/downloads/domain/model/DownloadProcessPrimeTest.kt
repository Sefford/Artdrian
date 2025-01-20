package com.sefford.artdrian.downloads.domain.model

import com.sefford.artdrian.common.data.network.HttpClient
import com.sefford.artdrian.test.FakeHttpClient
import com.sefford.artdrian.test.InjectableTest
import com.sefford.artdrian.test.LazyMockEngineHandler
import com.sefford.artdrian.test.mothers.DownloadsMother
import com.sefford.artdrian.test.respondOnly
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.shouldBe
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headers
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.nio.channels.UnresolvedAddressException
import java.nio.file.Files
import javax.inject.Inject
import io.ktor.client.HttpClient as KtorClient

class DownloadProcessPrimeTest : InjectableTest() {

    private val cache = Files.createTempDirectory("downloads").toFile()

    @Inject
    internal lateinit var client: KtorClient

    @Inject
    internal lateinit var delegatedClient: HttpClient

    @Inject
    internal lateinit var handlers: LazyMockEngineHandler

    @BeforeEach
    override fun beforeEach() {
        super.beforeEach()
        graph.inject(this)
    }

    @Test
    fun `returns Fetch when the download is Pending`() = runTest {
        givenAPrime(response = givenAResponse()).shouldBeRight()
    }

    @Test
    fun `returns Fetch when the download is Ongoing`() = runTest {
        givenAPrime(
            response = givenAResponse(),
            download = DownloadsMother.createOngoing(hash = HASH)
        ).shouldBeRight()
    }

    @Test
    fun `returns Fetch when the download is Ongoing and the upload got renewed`() = runTest {
        val response = givenAResponse()
        handlers.queue { request ->
            respondOnly(
                matcher = { !request.headers.contains(HttpHeaders.Range) },
                status = HttpStatusCode.OK,
                content = CONTENT,
                headers = HEADERS_RESPONSE
            )
        }

        givenAPrime(
            response = response,
            client = delegatedClient
        ).shouldBeRight()
    }


    @Test
    fun `returns Success when the download is Finished`() = runTest {
        val response = givenAResponse()

        givenAPrime(
            response = response,
            download = DownloadsMother.createFinished()
        ).shouldBeLeft() shouldBe DownloadProcess.Results.Success
    }

    @Test
    fun `returns failure when received an unexpected exception`() = runTest {
        val response = givenAResponse()

        givenAPrime(
            client = FakeHttpClient(client, { throw IllegalStateException() }),
            response = response,
            download = DownloadsMother.createOngoing()
        ).shouldBeLeft() shouldBe DownloadProcess.Results.Failure
    }

    @Test
    fun `returns retry when receiving a connectivity problem`() = runTest {
        givenAPrime(
            client = FakeHttpClient(client, { throw UnresolvedAddressException() }),
            response = givenAResponse(),
            download = DownloadsMother.createOngoing()
        ).shouldBeLeft() shouldBe DownloadProcess.Results.Retry
    }

    @Test
    fun `returns failure when the response is not successful and not retryable`() = runTest {
        val response = givenAResponse()
        handlers.queue { respondOnly(status = HttpStatusCode.NotFound, content = "") }

        givenAPrime(
            response = response,
            download = DownloadsMother.createOngoing()
        ).shouldBeLeft() shouldBe DownloadProcess.Results.Failure
    }

    @Test
    fun `returns retry when the response is retryable`() = runTest {
        val response = givenAResponse()

        handlers.queue { respondOnly(status = HttpStatusCode.ServiceUnavailable, content = "") }

        givenAPrime(
            response = response,
            download = DownloadsMother.createOngoing()
        ).shouldBeLeft() shouldBe DownloadProcess.Results.Retry
    }

    private suspend fun DownloadProcessPrimeTest.givenAPrime(
        client: HttpClient = FakeHttpClient(this.client),
        response: HttpResponse,
        download: Download = DownloadsMother.createPending()
    ) = DownloadProcess.Step.Prime(
        events = {},
        client = client,
        directory = cache,
        response = response,
        download = download
    ).ready()

    private suspend fun givenAResponse(): HttpResponse {
        handlers.queue { request ->
            respondOnly(
                status = HttpStatusCode.OK,
                content = CONTENT,
                headers = HEADERS_RESPONSE
            )
        }

        val response = client.get("")
        return response
    }
}

private const val CONTENT = "This is really a wallpaper, promise!"
private const val HASH = "9cc769f284bba4616668623ca2c22f3e"
private val HEADERS_RESPONSE = headers {
    append(HttpHeaders.ETag, HASH)
    append(HttpHeaders.ContentDisposition, "inline; filename=\"ghost_waves_004.jpg\"")
    append(HttpHeaders.ContentLength, "36")
}
