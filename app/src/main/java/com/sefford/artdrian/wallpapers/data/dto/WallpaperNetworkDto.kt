package com.sefford.artdrian.wallpapers.data.dto

import com.sefford.artdrian.wallpapers.data.dto.deserializers.DateDeserializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class WallpaperNetworkDto(
    @SerialName("_id") val id: String,
    @SerialName("slug") val slug: String,
    @SerialName("edition") val version: String,
    @SerialName("title") val title: String,
    @SerialName("totalDownloads") val downloads: Int,
    @SerialName("image") val preview: String,
    @SerialName("imageExtension") val extension: String,
    @SerialName("tags") val tags: List<String>,
    @SerialName("publishedAt") @Serializable(with = DateDeserializer::class) val published: LocalDateTime,
) {
    override fun toString(): String {
        return "MetadataDto(title='$title', slug='$slug')"
    }
}

