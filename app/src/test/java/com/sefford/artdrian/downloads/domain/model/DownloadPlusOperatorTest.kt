package com.sefford.artdrian.downloads.domain.model

import com.sefford.artdrian.test.assertions.shouldBeOfSize
import com.sefford.artdrian.test.mothers.DownloadsMother
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.jupiter.api.Test

class DownloadPlusOperatorTest {

    @Test
    fun `pending keeps stable`() {
        (DownloadsMother.createPending() + DownloadsMother.createPending()).shouldBeInstanceOf<Download.Pending>()
    }

    @Test
    fun `pending gets overridden by a primed`() {
        (DownloadsMother.createPending() + DownloadsMother.createPrimed()).shouldBeInstanceOf<Download.Primed>()
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
    fun `primed gets updated by a different total`() {
        (DownloadsMother.createPrimed() + DownloadsMother.createPrimed(total = 1250)).should { download ->
            download.shouldBeInstanceOf<Download.Primed>()
            download.total shouldBeOfSize 1250
        }
    }

    @Test
    fun `primed gets updated by a different hash`() {
        (DownloadsMother.createPrimed() + DownloadsMother.createPrimed(hash = "4321")).should { download ->
            download.shouldBeInstanceOf<Download.Primed>()
            download.hash shouldBe "4321"
        }
    }

    @Test
    fun `primed does not get overridden by a pending`() {
        (DownloadsMother.createPrimed() + DownloadsMother.createPending()).shouldBeInstanceOf<Download.Primed>()
    }

    @Test
    fun `primed gets overridden by an ongoing`() {
        (DownloadsMother.createPrimed() + DownloadsMother.createOngoing()).shouldBeInstanceOf<Download.Ongoing>()
    }

    @Test
    fun `primed gets overridden by a finished`() {
        (DownloadsMother.createPending() + DownloadsMother.createFinished()).shouldBeInstanceOf<Download.Finished>()
    }

    @Test
    fun `ongoing gets updated by an updated ongoing`() {
        (DownloadsMother.createOngoing() + DownloadsMother.createOngoing(downloaded = 500)).should { download ->
            download.shouldBeInstanceOf<Download.Ongoing>()
            download.progress shouldBeOfSize  500
        }
    }

    @Test
    fun `ongoing keeps the higher progress`() {
        (DownloadsMother.createOngoing(downloaded = 500) + DownloadsMother.createOngoing()).should { download ->
            download.shouldBeInstanceOf<Download.Ongoing>()
            download.progress shouldBeOfSize 500
        }
    }

    @Test
    fun `ongoing does not get overridden by a pending`() {
        (DownloadsMother.createOngoing() + DownloadsMother.createPending()).shouldBeInstanceOf<Download.Ongoing>()
    }

    @Test
    fun `ongoing does not get overridden by a primed`() {
        (DownloadsMother.createOngoing() + DownloadsMother.createPrimed()).shouldBeInstanceOf<Download.Ongoing>()
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
    fun `finished does not get overridden by a primed`() {
        (DownloadsMother.createFinished() + DownloadsMother.createPrimed()).shouldBeInstanceOf<Download.Finished>()
    }

    @Test
    fun `finished does not get overridden by a ongoing`() {
        (DownloadsMother.createFinished() + DownloadsMother.createOngoing()).shouldBeInstanceOf<Download.Finished>()
    }


}
