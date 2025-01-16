package com.sefford.artdrian.common.language.units

import com.sefford.artdrian.common.language.units.Size.Companion.GBs
import com.sefford.artdrian.common.language.units.Size.Companion.MBs
import com.sefford.artdrian.common.language.units.Size.Companion.PBs
import com.sefford.artdrian.common.language.units.Size.Companion.bytes
import com.sefford.artdrian.common.language.units.Size.Companion.kBs
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class SizeConversionTest {

    @Test
    fun `converts to bytes`() {
        1L.bytes.inBytes shouldBe 1L
    }

    @Test
    fun `converts to kBs`() {
        1L.kBs.inBytes shouldBe ONE_KB_IN_BYTES
    }

    @Test
    fun `converts to MBs`() {
        1L.MBs.inBytes shouldBe ONE_MB_IN_BYTES
    }
    @Test
    fun `converts to GBs`() {
        1L.GBs.inBytes shouldBe ONE_GB_IN_BYTES
    }

    @Test
    fun `converts to PBs`() {
        1L.PBs.inBytes shouldBe ONE_PB_IN_BYTES
    }
}

private const val ONE_KB_IN_BYTES = 1024L
private const val ONE_MB_IN_BYTES = 1048576L
private const val ONE_GB_IN_BYTES = 1073741824L
private const val ONE_PB_IN_BYTES = 1099511627776L
