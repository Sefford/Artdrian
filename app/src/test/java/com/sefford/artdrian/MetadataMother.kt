package com.sefford.artdrian

import com.sefford.artdrian.data.dto.MetadataDto
import com.sefford.artdrian.model.Metadata
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime


object MetadataMother {

    const val FIRST_METADATA_ID = "1"
    const val SECOND_METADATA_ID = "2"
    val FIRST_METADATA_DTO = MetadataDto(
        id = FIRST_METADATA_ID,
        slug = "test_001",
        title = "test_001",
        downloads = 456,
        created = Clock.System.now().toLocalDateTime(TimeZone.UTC),
    )
    val SECOND_METADATA = Metadata(
        id = SECOND_METADATA_ID,
        title = "test_002",
        slug = "test_002",
        views = 100,
        downloads = 200,
        created = Clock.System.now().toLocalDateTime(TimeZone.UTC)
    )
    val GHOST_WAVES_003 = Metadata(
        id = SECOND_METADATA_ID,
        title = "ghost_waves_003",
        slug = "ghost_waves_003",
        views = 100,
        downloads = 200,
        created = Clock.System.now().toLocalDateTime(TimeZone.UTC)
    )
    val EXAMPLE_METADATA = listOf(
        SECOND_METADATA
    )
}
