package com.sefford.artdrian.downloads.store

import com.sefford.artdrian.notifications.model.Notifications
import com.sefford.artdrian.test.mothers.DownloadsMother
import com.sefford.artdrian.test.unconfine
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainOnly
import io.kotest.matchers.collections.shouldHaveSize
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class DownloaderBridgeTest {
    private val urls = mutableListOf<String>()
    private val effects = MutableSharedFlow<DownloadsEffects>()

    @Test
    fun `links correctly`() = runTest {
        effects.bridgeDownloader(urls::add, backgroundScope.unconfine())

        effects.emit(DownloadsEffects.Register(DownloadsMother.createPending()))

        urls.shouldContainOnly(DownloadsMother.createPending().url)
    }

    @Test
    fun `filters other effects`() = runTest {
        effects.bridgeDownloader(urls::add, backgroundScope.unconfine())

        effects.emit(DownloadsEffects.LoadAll)
        effects.emit(DownloadsEffects.Update(DownloadsMother.createPending()))
        effects.emit(DownloadsEffects.Notify(DownloadsMother.createPending()))

        urls.shouldBeEmpty()
    }

}
