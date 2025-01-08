package com.sefford.artdrian.wallpapers.data.dto.deserializers

import com.sefford.artdrian.test.Resources
import com.karumi.kotlinsnapshot.matchWithSnapshot
import com.sefford.artdrian.wallpapers.data.dto.WallpaperResponse
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test

class WallpaperResponseDeserializerTest : Resources {

    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    @Test
    fun deserialize() {
        val response = json.decodeFromString<WallpaperResponse>("single-wallpaper-response.json".asResponse())

        response.matchWithSnapshot()
    }
}
