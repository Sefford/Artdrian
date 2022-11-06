package com.sefford.artdrian

import java.time.LocalDate
import java.time.ZoneOffset
import java.util.*


object MetadataMother {

    const val FIRST_METADATA_ID = "1"
    val EXAMPLE_METADATA = listOf(
        com.sefford.artdrian.model.Metadata(
            id = FIRST_METADATA_ID,
            slug = "test_001",
            views = 123,
            downloads = 456,
            created = Date.from(LocalDate.parse("2022-11-05").atStartOfDay().toInstant(ZoneOffset.UTC)),
            updated = Date.from(LocalDate.parse("2022-11-05").atStartOfDay().toInstant(ZoneOffset.UTC)),
        )
    )
}
