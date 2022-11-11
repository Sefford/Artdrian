package com.sefford.artdrian.model

import com.karumi.kotlinsnapshot.matchWithSnapshot
import com.sefford.artdrian.MetadataMother
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import net.jqwik.api.*
import org.junit.jupiter.api.Test


class WallpaperTest {

    @Test
    fun `Ghost Waves 003 indicates it is a JPG wallpaper`() {
        Wallpaper(MetadataMother.GHOST_WAVES_003).metadata.isPngFile().shouldBeFalse()
    }

    @Property
    fun `any other wallpaper indicates it is a PNG wallpaper`(@ForAll("slugs") slug: String) {
        Wallpaper(MetadataMother.FIRST_METADATA.copy(slug = slug)).metadata.isPngFile().shouldBeTrue()
    }

    @Provide
    fun slugs(): Arbitrary<String> = Arbitraries.strings().filter { slug -> slug != MetadataMother.GHOST_WAVES_003.slug }
}
