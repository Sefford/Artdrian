package com.sefford.artdrian.common.language

import io.kotest.matchers.collections.shouldContainOnly
import org.junit.jupiter.api.Test

class CollectionsTest {

    @Test
    fun `intersection contains only the common elements`() {
        listOf(1, 2).intersect(listOf(2, 3)).shouldContainOnly(2)
    }

    @Test
    fun `outer join does not contain the intersection elements`() {
        listOf(1, 2).outerJoin(listOf(2, 3)).shouldContainOnly(1, 3)
    }

    @Test
    fun `difference keeps only the unique elements of left`() {
        (listOf(1, 2) - listOf(2, 3)).shouldContainOnly(1)
    }
}
