package com.sefford.artdrian.downloads.store

import com.sefford.artdrian.downloads.domain.model.Download
import com.sefford.artdrian.test.mothers.DownloadsMother
import com.sefford.artdrian.test.unconfine
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class DownloaderBridgeTest {

    @Test
    fun `links correctly`() = runTest {
        val received = mutableListOf<Download>()
        val queue: (Download) -> Unit = { download: Download -> received.add(download) }

        MutableStateFlow(DownloadsState.Loaded(listOf(DownloadsMother.createPending())))
            .bridgeDownloader(queue, backgroundScope.unconfine()
        )

        received.shouldHaveSize(1)
    }

    @Test
    fun `filters wrong state`() = runTest {
        val received = mutableListOf<Download>()
        val queue: (Download) -> Unit = { download: Download -> received.add(download) }

        MutableStateFlow(DownloadsState.Preload(listOf(DownloadsMother.createPending())))
            .bridgeDownloader(queue, backgroundScope.unconfine()
            )

        received.shouldBeEmpty()
    }

    @Test
    fun `filters finished downloads`() = runTest {
        val received = mutableListOf<Download>()
        val queue: (Download) -> Unit = { download: Download -> received.add(download) }

        MutableStateFlow(DownloadsState.Loaded(listOf(DownloadsMother.createFinished())))
            .bridgeDownloader(queue, backgroundScope.unconfine()
            )

        received.shouldBeEmpty()
    }
}
