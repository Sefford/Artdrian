package com.sefford.artdrian.test

import com.sefford.artdrian.data.dto.WallpaperDatabaseDto

object DatabaseDtoMother {
    fun createWallpaper(
        id: String = "1",
        title: String = "Ghost Waves",
        views: Int = 9284,
        downloads: Int = 31520,
        slug: String = "default-slug",
        created: String = "2024-01-01T00:00:00",
        updated: String = "2024-01-01T01:00:00"
    ): WallpaperDatabaseDto {
        return WallpaperDatabaseDto(
            id = id,
            title = title,
            views = views,
            downloads = downloads,
            slug = slug,
            created = created,
            updated = updated
        )
    }
}
