package com.sefford.artdrian.data.dto

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MetadataDto(
  @SerialName("_id") val id: String,
  @SerialName("slug") val slug: String,
  @SerialName("title") val title: String,
  @SerialName("totalDownloads") val downloads: Int,
  @SerialName("publishedAt") val created: LocalDateTime,
)

