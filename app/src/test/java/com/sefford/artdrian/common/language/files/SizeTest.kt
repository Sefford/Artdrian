package com.sefford.artdrian.common.language.files

import com.sefford.artdrian.common.language.files.Size.Companion.bytes
import io.kotest.matchers.booleans.shouldBeTrue
import org.junit.jupiter.api.Test

class SizeTest {

    @Test
    fun `0 bytes is zero`() {
        0L.bytes.isZero.shouldBeTrue()
    }
}
