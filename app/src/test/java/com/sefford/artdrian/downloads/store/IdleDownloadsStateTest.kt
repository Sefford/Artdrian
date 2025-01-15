package com.sefford.artdrian.downloads.store

import com.sefford.artdrian.common.data.DataError
import com.sefford.artdrian.test.mothers.DownloadsMother
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.should
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.jupiter.api.Test

class IdleDownloadsStateTest {
    @Test
    fun `Idle plus Empty equals empty`() {
        (DownloadsState.Idle + listOf()).shouldBeInstanceOf<DownloadsState.Empty>()
    }

    @Test
    fun `Idle plus Error equals empty`() {
        (DownloadsState.Idle + ERROR).shouldBeInstanceOf<DownloadsState.Empty>()
    }

    @Test
    fun `Idle plus Empty Preload equals Idle`() {
        (DownloadsState.Idle + DownloadsState.Preload(listOf())).shouldBeInstanceOf<DownloadsState.Idle>()
    }

    @Test
    fun `Idle plus Preload equals Preload`() {
        (DownloadsState.Idle + DownloadsState.Preload(listOf(DownloadsMother.createPending()))).should { preload ->
            preload.shouldBeInstanceOf<DownloadsState.Preload>()
            preload.downloads.shouldHaveSize(1)
        }
    }

    @Test
    fun `Idle plus empty Downloads equals Empty`() {
        (DownloadsState.Idle + listOf()).shouldBeInstanceOf<DownloadsState.Empty>()
    }

    @Test
    fun `Idle plus Downloads equals Loaded`() {
        (DownloadsState.Idle + listOf(DownloadsMother.createPending())).should { loaded ->
            loaded.shouldBeInstanceOf<DownloadsState.Loaded>()
            loaded.downloads.shouldHaveSize(1)
        }
    }
}

private val ERROR = DataError.Local.Critical(RuntimeException())

