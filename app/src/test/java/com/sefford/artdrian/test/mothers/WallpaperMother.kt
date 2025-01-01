package com.sefford.artdrian.test.mothers

import com.sefford.artdrian.model.Wallpaper
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.LocalDateTime as JavaLocalDateTime

object WallpaperMother {

    fun generateLocal(
        id: String = "1",
        title: String = "Ghost Waves 004",
        views: Int = 9284,
        downloads: Int = 31520,
        slug: String = "default-slug",
        created: LocalDateTime = JavaLocalDateTime.of(1984, 9, 2, 16, 45).toKotlinLocalDateTime(),
        updated: LocalDateTime = created,
    ) = Wallpaper.FromLocal(id, title, views, downloads, slug, created, updated)

    fun generateNetwork(
        id: String = "1",
        title: String = "Ghost Waves 004",
        views: Int = 9284,
        downloads: Int = 31520,
        slug: String = "default-slug",
        created: LocalDateTime = JavaLocalDateTime.of(1984, 9, 2, 16, 45).toKotlinLocalDateTime(),
        updated: LocalDateTime = created,
    ) = Wallpaper.FromNetwork(id, title, views, downloads, slug, created, updated)
}
