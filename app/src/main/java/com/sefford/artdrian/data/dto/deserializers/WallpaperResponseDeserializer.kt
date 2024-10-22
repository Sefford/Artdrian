package com.sefford.artdrian.data.dto.deserializers

import com.sefford.artdrian.data.dto.MetadataDto
import com.sefford.artdrian.data.dto.WallpaperResponse
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject

class WallpaperResponseDeserializer : KSerializer<WallpaperResponse> {

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("WallpaperResponse", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): WallpaperResponse {
        val jsonDecoder = (decoder as JsonDecoder)
        val jsonObject = jsonDecoder.decodeJsonElement().jsonObject
        return WallpaperResponse(
            jsonObject["pageProps"]!!
                .jsonObject["wallpapers"]!!
                .jsonArray
                .map { e -> jsonDecoder.json.decodeFromJsonElement(MetadataDto.serializer(), e) }
        )
    }

    override fun serialize(encoder: Encoder, value: WallpaperResponse) {
        TODO("Not yet implemented")
    }
}