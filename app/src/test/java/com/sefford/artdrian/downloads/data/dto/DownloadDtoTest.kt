package com.sefford.artdrian.downloads.data.dto

import com.sefford.artdrian.test.mothers.DownloadsDtoMother
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.should
import org.junit.jupiter.api.Test

class DownloadDtoTest {

    @Test
    fun `a pending download has the correct denomination`() {
        DownloadsDtoMother.createPending().should { download ->
            download.pending.shouldBeTrue()
            download.primed.shouldBeFalse()
            download.ongoing.shouldBeFalse()
            download.completed.shouldBeFalse()
        }
    }

    @Test
    fun `a primed download has the correct denomination`() {
        DownloadsDtoMother.createOngoing().should { download ->
            download.pending.shouldBeFalse()
            download.primed.shouldBeFalse()
            download.ongoing.shouldBeTrue()
            download.completed.shouldBeFalse()
        }
    }

    @Test
    fun `a finished download has the correct denomination`() {
        DownloadsDtoMother.createFinished().should { download ->
            download.pending.shouldBeFalse()
            download.primed.shouldBeFalse()
            download.ongoing.shouldBeFalse()
            download.completed.shouldBeTrue()
        }
    }
}
