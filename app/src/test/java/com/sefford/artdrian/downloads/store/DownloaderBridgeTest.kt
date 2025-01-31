package com.sefford.artdrian.downloads.store

import com.sefford.artdrian.downloads.domain.model.Download
import com.sefford.artdrian.test.mothers.DownloadsMother
import com.sefford.artdrian.test.unconfine
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class DownloaderBridgeTest {

    @Test
    fun `links correctly`() = runTest {
        val received = mutableListOf<String>()

        MutableStateFlow(DownloadsMother.createPending().url)
            .bridgeDownloader(received::add, backgroundScope.unconfine()
        )

        received.shouldHaveSize(1)
    }

}
