package com.sefford.artdrian.downloads.domain.model

import com.sefford.artdrian.downloads.store.DownloadsState
import com.sefford.artdrian.test.FakeLogger
import com.sefford.artdrian.test.networking.FakeHttpClient
import com.sefford.artdrian.test.InjectableTest
import com.sefford.artdrian.test.mothers.DownloadsMother
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.ktor.client.HttpClient
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.nio.file.Files
import javax.inject.Inject

class DownloadProcessViabilityTest : InjectableTest() {

    private val cache = Files.createTempDirectory("downloads").toFile()

    @Inject
    internal lateinit var client: HttpClient

    private val logger = FakeLogger()

    @BeforeEach
    override fun beforeEach() {
        super.beforeEach()
        graph.inject(this)
    }

    @Test
    fun `returns failure when the ID state is Empty`() {
        givenAViabilityStep(state = DownloadsState.Empty).should { result ->
            result.shouldBeLeft()
            result.value shouldBe DownloadProcess.Results.Failure
        }
    }

    @Test
    fun `returns failure when the ID cannot be found`() {
        givenAViabilityStep(state = DownloadsState.Loaded(emptyList())).should { result ->
            result.shouldBeLeft()
            result.value shouldBe DownloadProcess.Results.Failure
        }
    }

    @Test
    fun `returns retry when the downloads are Idle`() {
        givenAViabilityStep(state = DownloadsState.Idle).should { result ->
            result.shouldBeLeft()
            result.value shouldBe DownloadProcess.Results.Retry
        }
    }

    @Test
    fun `returns retry when the downloads are Preloaded`() {
        givenAViabilityStep(state = DownloadsState.Preload(emptyList())).should { result ->
            result.shouldBeLeft()
            result.value shouldBe DownloadProcess.Results.Retry
        }
    }

    @Test
    fun `returns success when the download is already finished`() {
        givenAViabilityStep(state = DownloadsState.Loaded(listOf(DownloadsMother.createFinished()))).should { result ->
            result.shouldBeLeft()
            result.value shouldBe DownloadProcess.Results.Success
        }
    }

    @Test
    fun `proceeds with next step when the download is on a viable state`() {
        givenAViabilityStep(state = DownloadsState.Loaded(listOf(DownloadsMother.createPending()))).should { result ->
            result.shouldBeRight()
            result.value.shouldBeInstanceOf<DownloadProcess.Step.Probe>()
        }
    }

    private fun givenAViabilityStep(
        state: DownloadsState = DownloadsState.Empty
    ) =
        DownloadProcess.Step.Viability(
            client = FakeHttpClient(client),
            downloads = state,
            log = logger::log ,
            events = {},
            directory = cache,
            url = DownloadsMother.createPending().url
        ).check()
}

