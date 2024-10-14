package com.sefford.artdrian.data.dto.deserializers

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.karumi.kotlinsnapshot.matchWithSnapshot
import com.sefford.artdrian.data.dto.WallpaperResponse
import com.sefford.utils.Files

import org.junit.jupiter.api.Test

class WallpaperResponseDeserializerTest : Files {

    private val gson : Gson = GsonBuilder()
        .registerTypeAdapter(WallpaperResponse::class.java, WallpaperResponseDeserializer())
        .create()

    @Test
    fun deserialize() {
        val response = gson.fromJson(
            this@WallpaperResponseDeserializerTest::class.java.readResourceFromFile("index.json"),
            WallpaperResponse::class.java)

        response.matchWithSnapshot()
    }
}