package com.sefford.artdrian.common.language.units

import com.sefford.artdrian.common.language.units.Size.Companion.GBs
import com.sefford.artdrian.common.language.units.Size.Companion.MBs
import com.sefford.artdrian.common.language.units.Size.Companion.PBs
import com.sefford.artdrian.common.language.units.Size.Companion.bytes
import com.sefford.artdrian.common.language.units.Size.Companion.kBs
import io.kotest.matchers.longs.shouldBeZero
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class SizeRepresentationTest {

    @Test
    fun `represents whole bytes`() {
        1L.bytes.inBytes shouldBe 1L
    }

    @Test
    fun `represents whole kBs`() {
        1L.kBs.inWholekBs shouldBe 1L
    }

    @Test
    fun `represents whole MBs`() {
        1L.MBs.inWholeMBs shouldBe 1L
    }
    @Test
    fun `represents whole GBs`() {
        1L.GBs.inWholeGBs shouldBe 1L
    }

    @Test
    fun `represents whole PBs`() {
        1L.PBs.inWholePBs shouldBe 1L
    }

    @Test
    fun `representing a smaller unit is zero`() {
        val size = 1L.bytes

        size.inWholekBs.shouldBeZero()
        size.inWholeMBs.shouldBeZero()
        size.inWholeGBs.shouldBeZero()
        size.inWholePBs.shouldBeZero()
    }

    @Test
    fun `representing a bigger unit is not zero`() {
        val size = 1L.PBs

        size.inBytes shouldBe ONE_PB_IN_BYTES
        size.inWholekBs shouldBe ONE_PB_IN_KBS
        size.inWholeMBs shouldBe ONE_PB_IN_MBS
        size.inWholeGBs shouldBe ONE_PB_IN_GBS
    }
}

private const val ONE_PB_IN_BYTES = 1099511627776L
private const val ONE_PB_IN_KBS = 1073741824L
private const val ONE_PB_IN_MBS = 1048576L
private const val ONE_PB_IN_GBS = 1024L
