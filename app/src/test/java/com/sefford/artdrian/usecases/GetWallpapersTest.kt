package com.sefford.artdrian.usecases

import com.google.gson.Gson
import com.karumi.kotlinsnapshot.matchWithSnapshot
import com.sefford.artdrian.data.datasources.WallpaperApi
import com.sefford.artdrian.data.datasources.WallpaperMemoryDataSource
import com.sefford.artdrian.data.datasources.WallpaperRepository
import com.sefford.utils.Files
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@OptIn(ExperimentalCoroutinesApi::class)
class GetWallpapersTest : Files {

    private lateinit var useCase: GetWallpapers
    private lateinit var server: MockWebServer

    @BeforeEach
    fun setUp() {
        server = MockWebServer()
        server.start()
        useCase = GetWallpapers(WallpaperRepository(initializeApi(), WallpaperMemoryDataSource()))
    }

    private fun initializeApi(): WallpaperApi =
        Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
            .create(WallpaperApi::class.java)

    @Test
    fun `returns the wallpapers`() = runTest {
        server.enqueue(
            MockResponse().setResponseCode(200).setBody(this@GetWallpapersTest::class.java.readResourceFromFile("metadata_response.json"))
        )

        useCase.getWallpapers().matchWithSnapshot()
    }
}
