package com.sefford.artdrian.data.dto

import com.sefford.artdrian.data.dto.deserializers.DateDeserializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class WallpaperNetworkDto(
    @SerialName("_id") val id: String,
    @SerialName("slug") val slug: String,
    @SerialName("title") val title: String,
    @SerialName("totalDownloads") val downloads: Int,
    @SerialName("publishedAt") @Serializable(with = DateDeserializer::class) val created: LocalDateTime,
) {
    override fun toString(): String {
        return "MetadataDto(title='$title', slug='$slug')"
    }
}

