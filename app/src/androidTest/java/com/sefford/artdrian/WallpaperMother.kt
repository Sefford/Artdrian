package com.sefford.artdrian

import com.sefford.artdrian.data.dto.WallpaperNetworkDto
import com.sefford.artdrian.model.Wallpaper
import kotlinx.datetime.LocalDateTime

object WallpaperMother {
    const val FIRST_ID = "wallpapers/ghost_waves_004.mdx"

    val WALLPAPER_LIST_DTO = listOf(

        WallpaperNetworkDto(
            id = FIRST_ID,
            slug = "ghost_waves_001",
            title = "ghost_waves_001",
            downloads = 456,
            created = LocalDateTime.parse("2022-11-05T00:00:00"),
        ),

        WallpaperNetworkDto(
            id = "2",
            slug = "ghost_waves_002",
            title = "ghost_waves_002",

            downloads = 200,
            created = LocalDateTime.parse("2022-11-05T00:00:00"),
        )
    )

    val WALLPAPER_LIST = listOf(
        Wallpaper(
            Wallpaper(
                id = FIRST_ID,
                views = 123,
                slug = "ghost_waves_001",
                title = "ghost_waves_001",
                downloads = 456,
                created = LocalDateTime.parse("2022-11-05T00:00:00"),
                updated = LocalDateTime.parse("2022-11-05T00:00:00"),
            ),
        ),
        Wallpaper(
            Wallpaper(
                id = "2",
                views = 123,
                slug = "ghost_waves_002",
                title = "ghost_waves_002",
                downloads = 200,
                created = LocalDateTime.parse("2022-11-05T00:00:00"),
                updated = LocalDateTime.parse("2022-11-05T00:00:00"),
            )
        )
    )
}
