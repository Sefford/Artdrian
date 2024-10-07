package com.sefford.artdrian

import com.sefford.artdrian.data.dto.MetadataDto
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.*
import com.sefford.artdrian.model.Metadata


object MetadataMother {

    const val FIRST_METADATA_ID = "1"
    const val SECOND_METADATA_ID = "2"
    val FIRST_METADATA_DTO = MetadataDto(
        id = FIRST_METADATA_ID,
        slug = "test_001",
        title = "test_001",
        downloads = 456,
        created = Date.from(LocalDate.parse("2022-11-05").atStartOfDay().toInstant(ZoneOffset.UTC)),
    )
    val SECOND_METADATA = Metadata(
        id = SECOND_METADATA_ID,
        title = "test_002",
        slug = "test_002",
        views = 100,
        downloads = 200,
        created = Date.from(LocalDate.parse("2022-11-05").atStartOfDay().toInstant(ZoneOffset.UTC)),
        updated = Date.from(LocalDate.parse("2022-11-05").atStartOfDay().toInstant(ZoneOffset.UTC)),
    )
    val GHOST_WAVES_003 = Metadata(
        id = SECOND_METADATA_ID,
        title = "ghost_waves_003",
        slug = "ghost_waves_003",
        views = 100,
        downloads = 200,
        created = Date.from(LocalDate.parse("2022-11-05").atStartOfDay().toInstant(ZoneOffset.UTC)),
        updated = Date.from(LocalDate.parse("2022-11-05").atStartOfDay().toInstant(ZoneOffset.UTC)),
    )
    val EXAMPLE_METADATA = listOf(
        SECOND_METADATA
    )
}
