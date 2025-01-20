package com.sefford.artdrian.common.language.files

import com.sefford.artdrian.common.language.files.Size.Companion.GBs
import com.sefford.artdrian.common.language.files.Size.Companion.MBs
import com.sefford.artdrian.common.language.files.Size.Companion.PBs
import com.sefford.artdrian.common.language.files.Size.Companion.bytes
import com.sefford.artdrian.common.language.files.Size.Companion.kBs
import com.sefford.artdrian.downloads.store.extensions.LocaleExtension
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(LocaleExtension::class)
class SizeFormattingTest {

    @Test
    fun `formats 0 bytes`() {
        0L.bytes.toString() shouldBe "0 bytes"
    }


    @Test
    fun `formats a byte`() {
        1L.bytes.toString() shouldBe "1 byte"
    }

    @Test
    fun `formats bytes`() {
        256L.bytes.toString() shouldBe "256 bytes"
    }

    @Test
    fun `formats a kB`() {
        1L.kBs.toString() shouldBe "1 kB"
    }

    @Test
    fun `formats kBs`() {
        256L.kBs.toString() shouldBe "256 kBs"
    }

    @Test
    fun `formats decimal kBs`() {
        1.5.kBs.toString() shouldBe "1.5 kBs"
    }

    @Test
    fun `formats a MB`() {
        1L.MBs.toString() shouldBe "1 MB"
    }

    @Test
    fun `formats MBs`() {
        256L.MBs.toString() shouldBe "256 MBs"
    }

    @Test
    fun `formats decimal MBs`() {
        1.5.MBs.toString() shouldBe "1.5 MBs"
    }

    @Test
    fun `formats a GB`() {
        1L.GBs.toString() shouldBe "1 GB"
    }

    @Test
    fun `formats GBs`() {
        256L.GBs.toString() shouldBe "256 GBs"
    }

    @Test
    fun `formats decimal GBs`() {
        1.5.GBs.toString() shouldBe "1.5 GBs"
    }

    @Test
    fun `formats a PB`() {
        1L.PBs.toString() shouldBe "1 PB"
    }

    @Test
    fun `formats PBs`() {
        256L.PBs.toString() shouldBe "256 PBs"
    }

    @Test
    fun `formats decimal PBs`() {
        1.5.PBs.toString() shouldBe "1.5 PBs"
    }
}
