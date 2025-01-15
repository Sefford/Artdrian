package com.sefford.artdrian.downloads.effects

import arrow.core.right
import com.sefford.artdrian.downloads.domain.model.Download
import com.sefford.artdrian.downloads.store.DownloadsEffects
import com.sefford.artdrian.downloads.store.DownloadsEvents
import com.sefford.artdrian.test.mothers.DownloadsMother
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class DownloadsDomainEffectHandlerTest {

    @Test
    fun `loads all downloads`() = runTest {
        DownloadsDomainEffectHandler(getAllDownloads = { flowOf(ALL_DOWNLOADS.right()) }, scope = this)
            .handle(DownloadsEffects.LoadAll) { event ->
                event.shouldBeInstanceOf<DownloadsEvents.OnDownloadsReceived>()
            }
    }

    @Test
    fun `filters finished downloads`() = runTest {
        DownloadsDomainEffectHandler(getAllDownloads = { flowOf(ALL_DOWNLOADS.right()) }, scope = this)
            .handle(DownloadsEffects.LoadAll) { event ->
                event.shouldBeInstanceOf<DownloadsEvents.OnDownloadsReceived>()
                event.downloads.shouldHaveSize(1)
            }
    }


    @Test
    fun `persist a download`() = runTest {
        val persistDownloads: suspend (List<Download>) -> Unit = { downloads: List<Download> ->
            downloads.shouldHaveSize(2)
        }

        DownloadsDomainEffectHandler(persistDownloads = persistDownloads, scope = this)
            .handle(DownloadsEffects.Register(ALL_DOWNLOADS)) { }
    }
}

private val ALL_DOWNLOADS = listOf(DownloadsMother.createPending(), DownloadsMother.createFinished())
