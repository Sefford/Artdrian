package com.sefford.artdrian.data.dto

import com.google.gson.annotations.SerializedName
import java.util.Date

data class MetadataDto(
  @SerializedName("_id") val id: String,
  @SerializedName("slug") val slug: String,
  @SerializedName("Views") val views: Int,
  @SerializedName("Downloads") val downloads: Int,
  @SerializedName("CreatedAt") val created: Date,
  @SerializedName("UpdatedAt") val updated: Date
)

fun MetadataDto.isPngFile(): Boolean = this.slug.contains("png")

