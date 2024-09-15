package com.sefford.artdrian.data.dto

import com.google.gson.annotations.SerializedName
import com.sefford.artdrian.model.Metadata
import java.util.Date

data class MetadataDto(
    @SerializedName("ID") val id: String,
    @SerializedName("Slug") val slug: String,
    @SerializedName("Views") val views: Int,
    @SerializedName("Downloads") val downloads: Int,
    @SerializedName("CreatedAt") val created: Date,
    @SerializedName("UpdatedAt") val updated: Date
)

fun MetadataDto.toDomain(): Metadata =
    Metadata(
        id = this.id,
        slug = this.slug,
        views = this.views,
        downloads = this.downloads,
        created = this.created,
        updated = this.updated
    )