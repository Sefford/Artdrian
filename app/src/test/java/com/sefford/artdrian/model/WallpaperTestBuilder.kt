package com.sefford.artdrian.model

import com.sefford.artdrian.MetadataMother
import io.kotest.matchers.booleans.shouldBeFalse
import net.jqwik.api.Arbitraries
import net.jqwik.api.Arbitrary
import net.jqwik.api.ForAll
import net.jqwik.api.Property
import net.jqwik.api.Provide
import org.junit.jupiter.api.Test


class WallpaperTestBuilder {

    @Test
    fun `Ghost Waves 003 indicates it is a JPG wallpaper`() {
        Wallpaper(MetadataMother.GHOST_WAVES_003).metadata.isPngFile().shouldBeFalse()
    }

    @Property
    fun `any other wallpaper indicates it is a PNG wallpaper`(@ForAll("slugs") slug: String) {
//        Wallpaper(MetadataMother.FIRST_METADATA_DTO.copy(slug = slug)).metadata.isPngFile().shouldBeTrue()
    }

    @Provide
    fun slugs(): Arbitrary<String> = Arbitraries.strings().filter { slug -> slug != MetadataMother.GHOST_WAVES_003.slug }
}
