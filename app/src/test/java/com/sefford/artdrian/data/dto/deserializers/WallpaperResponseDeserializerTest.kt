package com.sefford.artdrian.data.dto.deserializers

import com.karumi.kotlinsnapshot.matchWithSnapshot
import com.sefford.artdrian.data.dto.WallpaperResponse
import com.sefford.utils.Files
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test

class WallpaperResponseDeserializerTest : Files {

    private val json = Json {
        ignoreUnknownKeys = true  // optional configurations
        prettyPrint = true
    }

    @Test
    fun deserialize() {
        val string = this@WallpaperResponseDeserializerTest::class.java.readResourceFromFile("index.json")
        val response = json.decodeFromString<WallpaperResponse>(string)

        response.matchWithSnapshot()
    }
}
