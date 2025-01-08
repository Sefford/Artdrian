package com.sefford.artdrian.wallpapers.data.dto

import com.sefford.artdrian.wallpapers.data.dto.deserializers.WallpaperResponseDeserializer
import kotlinx.serialization.Serializable

@JvmInline
@Serializable(with = WallpaperResponseDeserializer::class)
value class WallpaperResponse(val wallpaper: WallpaperNetworkDto)
