package com.sefford.artdrian.test.mothers

import com.sefford.artdrian.wallpapers.data.dto.WallpaperDatabaseDto

object WallpaperDtoMother {
    fun createWallpaper(
        id: String = "1",
        slug: String = "ghost_waves_001",
        version: String = "001",
        title: String = "Ghost Waves",
        downloads: Int = 31520,
        preview: String = "https://example.com/preview.jpg",
        desktop: String = "https://example.com/desktop.jpg",
        mobile: String = "https://example.com/mobile.jpg",
        tags: String = "4K-READY",
        published: String = "2024-01-01T00:00:00"
    ): WallpaperDatabaseDto {
        return WallpaperDatabaseDto(
            id = id,
            slug = slug,
            version = version,
            title = title,
            downloads = downloads,
            preview = preview,
            desktop = desktop,
            mobile = mobile,
            tags = tags,
            published = published
        )
    }
}

