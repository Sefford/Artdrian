package com.sefford.artdrian.downloads.store

import com.sefford.artdrian.common.data.DataError
import com.sefford.artdrian.test.mothers.DownloadsMother
import io.kotest.matchers.collections.shouldContainOnly
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.jupiter.api.Test

class IdleDownloadsStateTest {

    @Test
    fun `Idle plus Error equals empty`() {
        (DownloadsState.Idle + ERROR).shouldBeInstanceOf<DownloadsState.Empty>()
    }

    @Test
    fun `Idle plus Empty Preload equals Preloads`() {
        (DownloadsState.Idle + DownloadsState.Preload(emptySet())).shouldBeInstanceOf<DownloadsState.Preload>()
    }

    @Test
    fun `Idle plus Preload equals Idle`() {
        (DownloadsState.Idle + DownloadsState.Preload(setOf(DownloadsMother.createPending())))
            .shouldBeInstanceOf<DownloadsState.Preload>()
            .downloads.shouldContainOnly(DownloadsMother.createPending())
    }
}

private val ERROR = DataError.Local.Critical(RuntimeException())

