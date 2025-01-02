package com.sefford.artdrian.test.mothers

import com.sefford.artdrian.model.Images
import com.sefford.artdrian.model.Wallpaper
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.LocalDateTime as JavaLocalDateTime

object WallpaperMother {

    fun generateLocal(
        id: String = "1",
        slug: String = "ghost_waves",
        version: String = "001",
        title: String = "Ghost Waves 004",
        downloads: Int = 31520,
        images: Images = Images(
            preview = "https://example.com/preview.jpg",
            desktop = "https://example.com/desktop.jpg",
            mobile = "https://example.com/mobile.jpg"
        ),
        tags: List<String> = listOf("4K-READY"),
        published: LocalDateTime = JavaLocalDateTime.of(1984, 9, 2, 16, 45).toKotlinLocalDateTime()
    ) = Wallpaper.FromLocal(id, slug, version, title, downloads, images, tags, published)

    fun generateNetwork(
        id: String = "1",
        slug: String = "ghost_waves",
        version: String = "001",
        title: String = "Ghost Waves 004",
        downloads: Int = 31520,
        images: Images = Images(
            preview = "https://example.com/preview.jpg",
            desktop = "https://example.com/desktop.jpg",
            mobile = "https://example.com/mobile.jpg"
        ),
        tags: List<String> = listOf("4K-READY"),
        published: LocalDateTime = JavaLocalDateTime.of(1984, 9, 2, 16, 45).toKotlinLocalDateTime()
    ) = Wallpaper.FromNetwork(id, slug, version, title, downloads, images, tags, published)
}

