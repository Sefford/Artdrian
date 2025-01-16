package com.sefford.artdrian.test.assertions

import com.sefford.artdrian.common.language.files.Size
import io.kotest.matchers.longs.shouldBeExactly


fun Size.shouldBeZero(): Size {
    inBytes shouldBeExactly 0
    return this
}

infix fun Size.shouldBeOfSize(size: Size): Size {
    inBytes shouldBeExactly size.inBytes
    return this
}

infix fun Size.shouldBeOfSize(size: Int): Size {
    inBytes shouldBeExactly size.toLong()
    return this
}
