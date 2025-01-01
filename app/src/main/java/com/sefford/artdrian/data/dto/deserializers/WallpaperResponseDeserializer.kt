package com.sefford.artdrian.data.dto.deserializers

import com.sefford.artdrian.data.dto.MetadataDto
import com.sefford.artdrian.data.dto.WallpaperResponse
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject

class WallpaperResponseDeserializer : KSerializer<WallpaperResponse> {

    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("Response") {
            element("wallpaper", MetadataDto.serializer().descriptor)
        }

    override fun deserialize(decoder: Decoder): WallpaperResponse {
        val jsonDecoder = (decoder as JsonDecoder)
        val jsonObject = jsonDecoder.decodeJsonElement().jsonObject
        return jsonObject["pageProps"]!!
            .jsonObject["wallpaper"]!!
            .jsonArray
            .map { e ->  WallpaperResponse(jsonDecoder.json.decodeFromJsonElement(e)) }
            .first()
    }

    override fun serialize(encoder: Encoder, value: WallpaperResponse) {
        TODO("Not yet implemented")
    }
}
