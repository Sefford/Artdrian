package com.sefford.artdrian.downloads.domain.model

import com.sefford.artdrian.test.mothers.DownloadsMother
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.should
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.Test

class DownloadsTest {

    @Test
    fun `merges two downloads`() {
        (listOf(DownloadsMother.createPending())+ listOf(DownloadsMother.createFinished())).should { result ->
            result.shouldHaveSize(1)
            result.first().shouldBeInstanceOf<Download.Finished>()
        }
    }

    @Test
    fun `merges two different downloads`() {
        (listOf(DownloadsMother.createPending()) + listOf(DownloadsMother.createFinished("2"))).should { result ->
            result.shouldHaveSize(2)
        }
    }
}
