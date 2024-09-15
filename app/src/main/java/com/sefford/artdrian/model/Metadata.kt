package com.sefford.artdrian.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Metadata(
  val id: String,
  val slug: String,
  val views: Int,
  val downloads: Int,
  val created: Date,
  val updated: Date
)

fun Metadata.isPngFile(): Boolean = this.slug != "ghost_waves_003"

