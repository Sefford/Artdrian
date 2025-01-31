package com.sefford.artdrian.downloads.domain.model

import com.sefford.artdrian.test.mothers.DownloadsMother
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.jupiter.api.Test

class DownloadPlusOperatorTest {

    @Test
    fun `pending keeps stable`() {
        (DownloadsMother.createPending() + DownloadsMother.createPending()).shouldBeInstanceOf<Download.Pending>()
    }

    @Test
    fun `pending gets overridden by a ongoing`() {
        (DownloadsMother.createPending() + DownloadsMother.createOngoing()).shouldBeInstanceOf<Download.Ongoing>()
    }

    @Test
    fun `pending gets overridden by a finished`() {
        (DownloadsMother.createPending() + DownloadsMother.createFinished()).shouldBeInstanceOf<Download.Finished>()
    }

    @Test
    fun `primed gets overridden by a finished`() {
        (DownloadsMother.createPending() + DownloadsMother.createFinished()).shouldBeInstanceOf<Download.Finished>()
    }

    @Test
    fun `ongoing does not get overridden by a pending`() {
        (DownloadsMother.createOngoing() + DownloadsMother.createPending()).shouldBeInstanceOf<Download.Ongoing>()
    }

    @Test
    fun `ongoing gets overridden by a finished`() {
        (DownloadsMother.createOngoing() + DownloadsMother.createFinished()).shouldBeInstanceOf<Download.Finished>()
    }

    @Test
    fun `finished does not get overridden by a pending`() {
        (DownloadsMother.createFinished() + DownloadsMother.createPending()).shouldBeInstanceOf<Download.Finished>()
    }

    @Test
    fun `finished does not get overridden by a ongoing`() {
        (DownloadsMother.createFinished() + DownloadsMother.createOngoing()).shouldBeInstanceOf<Download.Finished>()
    }


}
