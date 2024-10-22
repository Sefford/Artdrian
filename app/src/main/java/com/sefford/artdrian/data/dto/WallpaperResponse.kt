package com.sefford.artdrian.data.dto

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class WallpaperResponse(val wallpapers: List<MetadataDto>)