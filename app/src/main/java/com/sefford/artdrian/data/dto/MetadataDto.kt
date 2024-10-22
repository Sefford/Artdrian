package com.sefford.artdrian.data.dto

import com.sefford.artdrian.data.dto.deserializers.DateDeserializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class MetadataDto(
    @SerialName("_id") val id: String,
    @SerialName("slug") val slug: String,
    @SerialName("title") val title: String,
    @SerialName("totalDownloads") val downloads: Int,
    @Serializable(DateDeserializer::class)
    @SerialName("publishedAt") val created: Date,
)

