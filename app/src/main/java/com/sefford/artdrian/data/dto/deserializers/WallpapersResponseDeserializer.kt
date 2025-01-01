package com.sefford.artdrian.data.dto.deserializers

import com.sefford.artdrian.data.dto.WallpaperNetworkDto
import com.sefford.artdrian.data.dto.WallpapersResponse
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject

class WallpapersResponseDeserializer : KSerializer<WallpapersResponse> {

    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("Response") {
            element("wallpapers", ListSerializer(WallpaperNetworkDto.serializer()).descriptor)
        }

    override fun deserialize(decoder: Decoder): WallpapersResponse {
        val jsonDecoder = (decoder as JsonDecoder)
        val jsonObject = jsonDecoder.decodeJsonElement().jsonObject
        return WallpapersResponse(
            jsonObject["pageProps"]!!
                .jsonObject["wallpapers"]!!
                .jsonArray
                .map { e -> jsonDecoder.json.decodeFromJsonElement(e) }
        )
    }

    override fun serialize(encoder: Encoder, value: WallpapersResponse) {
        TODO("Not yet implemented")
    }
}
