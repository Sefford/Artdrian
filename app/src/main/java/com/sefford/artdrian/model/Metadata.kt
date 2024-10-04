package com.sefford.artdrian.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Metadata(
  @SerializedName("_id") val id: String,
  @SerializedName("slug") val slug: String,
  @SerializedName("Views") val views: Int,
  @SerializedName("Downloads") val downloads: Int,
  @SerializedName("CreatedAt") val created: Date,
  @SerializedName("UpdatedAt") val updated: Date
)

fun Metadata.isPngFile(): Boolean = this.slug.contains("png")

