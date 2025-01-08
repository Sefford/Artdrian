package com.sefford.artdrian.wallpapers.data.dto

import com.sefford.artdrian.wallpapers.data.dto.deserializers.WallpapersResponseDeserializer
import kotlinx.serialization.Serializable

@JvmInline
@Serializable(with = WallpapersResponseDeserializer::class)
value class WallpapersResponse(val wallpapers: List<WallpaperNetworkDto>)
