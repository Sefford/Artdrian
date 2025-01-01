package com.sefford.artdrian.data.dto

import com.sefford.artdrian.data.dto.deserializers.WallpapersResponseDeserializer
import kotlinx.serialization.Serializable

@JvmInline
@Serializable(with = WallpapersResponseDeserializer::class)
value class WallpapersResponse(val wallpapers: List<MetadataDto>)
