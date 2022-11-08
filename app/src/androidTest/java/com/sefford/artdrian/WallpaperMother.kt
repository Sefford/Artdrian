package com.sefford.artdrian

import com.sefford.artdrian.model.Metadata
import com.sefford.artdrian.model.Wallpaper
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.*

object WallpaperMother {

    val WALLPAPER_LIST = listOf(
        Wallpaper(
            Metadata(
            id = "1",
            slug = "ghost_waves_001",
            views = 123,
            downloads = 456,
            created = Date.from(LocalDate.parse("2022-11-05").atStartOfDay().toInstant(ZoneOffset.UTC)),
            updated = Date.from(LocalDate.parse("2022-11-05").atStartOfDay().toInstant(ZoneOffset.UTC)),
        )),
        Wallpaper(
            Metadata(
                id = "2",
                slug = "ghost_waves_002",
                views = 100,
                downloads = 200,
                created = Date.from(LocalDate.parse("2022-11-05").atStartOfDay().toInstant(ZoneOffset.UTC)),
                updated = Date.from(LocalDate.parse("2022-11-05").atStartOfDay().toInstant(ZoneOffset.UTC)),
            )
        )

    )
}
