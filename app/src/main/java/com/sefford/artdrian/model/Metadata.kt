package com.sefford.artdrian.model

import com.sefford.artdrian.data.dto.MetadataDto
import kotlinx.datetime.LocalDateTime

class Metadata(
    val id: String,
    val title: String,
    val views: Int,
    val downloads: Int,
    val slug: String,
    val created: LocalDateTime,
    val updated: LocalDateTime = created,
) {

    constructor(dto: MetadataDto) : this(
        id = dto.id,
        title = dto.title,
        views = 0,
        downloads = dto.downloads,
        slug = dto.slug,
        created = dto.created //TODO: look up for this parameter in the api response
    )
}

fun Metadata.isPngFile(): Boolean = this.slug.contains("png")
