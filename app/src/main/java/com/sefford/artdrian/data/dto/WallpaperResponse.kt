package com.sefford.artdrian.data.dto

import com.sefford.artdrian.data.dto.deserializers.WallpaperResponseDeserializer
import kotlinx.serialization.Serializable

@JvmInline
@Serializable(with = WallpaperResponseDeserializer::class)
value class WallpaperResponse(val wallpaper: MetadataDto)
