package com.sefford.artdrian.data.dto

import com.google.gson.annotations.SerializedName
import java.util.Date

data class MetadataDto(
  @SerializedName("_id") val id: String,
  @SerializedName("slug") val slug: String,
  @SerializedName("title") val title: String,
  @SerializedName("totalDownloads") val downloads: Int,
  @SerializedName("publishedAt") val created: Date,
)

