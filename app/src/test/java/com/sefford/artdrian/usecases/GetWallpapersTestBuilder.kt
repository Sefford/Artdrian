package com.sefford.artdrian.usecases

import com.karumi.kotlinsnapshot.matchWithSnapshot
import com.sefford.artdrian.data.dto.WallpaperResponse
import com.sefford.artdrian.datasources.WallpaperApi
import com.sefford.artdrian.datasources.WallpaperMemoryDataSource
import com.sefford.artdrian.datasources.WallpaperRepository
import com.sefford.utils.Files
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetWallpapersTestBuilder : Files {

    private lateinit var useCase: GetWallpapers
    private lateinit var server: MockWebServer

    @BeforeEach
    fun setUp() {
        server = MockWebServer()
        server.start()
        useCase = GetWallpapers(WallpaperRepository(initializeApi(), WallpaperMemoryDataSource()))
    }

    private fun initializeApi(): WallpaperApi = object: WallpaperApi {
        override suspend fun getAllMetadata(): WallpaperResponse {
            TODO("Not yet implemented")
        }

    }

    @Test
    fun `returns the wallpapers`() = runTest {
        server.enqueue(
            MockResponse().setResponseCode(200).setBody(this@GetWallpapersTestBuilder::class.java.readResourceFromFile("metadata_response.json"))
        )

        useCase.getWallpapers().matchWithSnapshot()
    }
}
