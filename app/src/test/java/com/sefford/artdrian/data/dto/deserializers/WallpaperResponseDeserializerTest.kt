package com.sefford.artdrian.data.dto.deserializers

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.karumi.kotlinsnapshot.matchWithSnapshot
import com.sefford.artdrian.datasources.WallpaperApi
import com.sefford.artdrian.model.Metadata
import com.sefford.utils.Files
import io.kotest.inspectors.runTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.BeforeEach
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