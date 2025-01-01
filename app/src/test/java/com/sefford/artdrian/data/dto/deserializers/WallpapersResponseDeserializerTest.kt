package com.sefford.artdrian.data.dto.deserializers

import com.sefford.artdrian.test.Resources
import com.karumi.kotlinsnapshot.matchWithSnapshot
import com.sefford.artdrian.data.dto.WallpapersResponse
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test

class WallpapersResponseDeserializerTest : Resources {

    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    @Test
    fun deserialize() {
        val response = json.decodeFromString<WallpapersResponse>("single-wallpaper-list-response.json".asResponse())

        response.matchWithSnapshot()
    }
}
