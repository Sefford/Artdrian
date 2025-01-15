package com.sefford.artdrian.downloads.store

import com.sefford.artdrian.common.data.DataError
import com.sefford.artdrian.test.mothers.DownloadsMother
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.should
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.jupiter.api.Test

class EmptyDownloadsStateTest {

    @Test
    fun `Empty plus Error equals Empty`() {
        (DownloadsState.Empty + ERROR).shouldBeInstanceOf<DownloadsState.Empty>()
    }

    @Test
    fun `Empty plus Empty Preload equals Empty`() {
        (DownloadsState.Empty + DownloadsState.Preload(listOf())).shouldBeInstanceOf<DownloadsState.Empty>()
    }

    @Test
    fun `Empty plus Preload equals Loaded`() {
        (DownloadsState.Empty + DownloadsState.Preload(listOf(DownloadsMother.createPending()))).should { preload ->
            preload.shouldBeInstanceOf<DownloadsState.Loaded>()
            preload.downloads.shouldHaveSize(1)
        }
    }

    @Test
    fun `Empty plus empty Downloads equals Empty`() {
        (DownloadsState.Empty + listOf()).shouldBeInstanceOf<DownloadsState.Empty>()
    }

    @Test
    fun `Empty plus Downloads equals Loaded`() {
        (DownloadsState.Empty + listOf(DownloadsMother.createPending())).should { loaded ->
            loaded.shouldBeInstanceOf<DownloadsState.Loaded>()
            loaded.downloads.shouldHaveSize(1)
        }
    }
}

private val ERROR = DataError.Local.Critical(RuntimeException())

