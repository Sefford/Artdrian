package com.sefford.artdrian.downloads.store

import com.sefford.artdrian.common.data.DataError
import com.sefford.artdrian.test.mothers.DownloadsMother
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.jupiter.api.Test

class EmptyDownloadsStateTest {

    @Test
    fun `Empty plus Error equals Empty`() {
        (DownloadsState.Empty + ERROR).shouldBeInstanceOf<DownloadsState.Empty>()
    }

    @Test
    fun `Empty plus Empty Preload equals Empty`() {
        (DownloadsState.Empty + DownloadsState.Preload(emptySet())).shouldBeInstanceOf<DownloadsState.Empty>()
    }

    @Test
    fun `Empty plus Preload equals Empty`() {
        (DownloadsState.Empty + DownloadsState.Preload(setOf(DownloadsMother.createPending())))
            .shouldBeInstanceOf<DownloadsState.Empty>()
    }
}

private val ERROR = DataError.Local.Critical(RuntimeException())

