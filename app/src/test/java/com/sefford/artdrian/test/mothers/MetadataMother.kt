package com.sefford.artdrian.test.mothers

import com.sefford.artdrian.model.Metadata
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.LocalDateTime as JavaLocalDateTime

object MetadataMother {

    fun generate(
        id: String = "1",
        title: String = "Ghost Waves 004",
        views: Int = 9284,
        downloads: Int = 31520,
        slug: String = "default-slug",
        created: LocalDateTime = JavaLocalDateTime.of(1984, 9, 2, 16, 45).toKotlinLocalDateTime(),
        updated: LocalDateTime = created,
    ): Metadata {
        return Metadata(
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
