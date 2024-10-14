package com.sefford.artdrian

import com.sefford.artdrian.data.dto.MetadataDto
import com.sefford.artdrian.model.Metadata
import com.sefford.artdrian.model.Wallpaper
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.Date

object WallpaperMother {
    const val FIRST_ID = "1"

    val WALLPAPER_LIST_DTO = listOf(

        MetadataDto(
            id = FIRST_ID,
            slug = "ghost_waves_001",
            title = "ghost_waves_001",
            downloads = 456,
            created = Date.from(
                LocalDate.parse("2022-11-05").atStartOfDay().toInstant(ZoneOffset.UTC)
            ),
        ),

        MetadataDto(
            id = "2",
            slug = "ghost_waves_002",
            title = "ghost_waves_002",

            downloads = 200,
            created = Date.from(
                LocalDate.parse("2022-11-05").atStartOfDay().toInstant(ZoneOffset.UTC)
            ),
        )
    )

    val WALLPAPER_LIST = listOf(
        Wallpaper(
            Metadata(
                id = FIRST_ID,
                views = 123,
                slug = "ghost_waves_001",
                title = "ghost_waves_001",
                downloads = 456,
                created = Date.from(
                    LocalDate.parse("2022-11-05").atStartOfDay().toInstant(ZoneOffset.UTC)
                ),
                updated = Date.from(
                    LocalDate.parse("2022-11-05").atStartOfDay().toInstant(ZoneOffset.UTC)
                ),
            ),
        ),
        Wallpaper(
            Metadata(
                id = "2",
                views = 123,
                slug = "ghost_waves_002",
                title = "ghost_waves_002",
                downloads = 200,
                created = Date.from(
                    LocalDate.parse("2022-11-05").atStartOfDay().toInstant(ZoneOffset.UTC)
                ),
                updated = Date.from(
                    LocalDate.parse("2022-11-05").atStartOfDay().toInstant(ZoneOffset.UTC)
                ),
            )
        )
    )
}
