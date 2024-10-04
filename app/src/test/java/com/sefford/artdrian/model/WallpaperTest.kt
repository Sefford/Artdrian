package com.sefford.artdrian.model

import com.sefford.artdrian.MetadataMother
import com.sefford.artdrian.data.dto.isPngFile
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import net.jqwik.api.*
import org.junit.jupiter.api.Test


class WallpaperTest {

    @Test
    fun `Ghost Waves 003 indicates it is a JPG wallpaper`() {
        Wallpaper(MetadataMother.GHOST_WAVES_003).metadataDto.isPngFile().shouldBeFalse()
    }

    @Property
    fun `any other wallpaper indicates it is a PNG wallpaper`(@ForAll("slugs") slug: String) {
        Wallpaper(MetadataMother.FIRST_METADATA_DTO.copy(slug = slug)).metadataDto.isPngFile().shouldBeTrue()
    }

    @Provide
    fun slugs(): Arbitrary<String> = Arbitraries.strings().filter { slug -> slug != MetadataMother.GHOST_WAVES_003.slug }
}
