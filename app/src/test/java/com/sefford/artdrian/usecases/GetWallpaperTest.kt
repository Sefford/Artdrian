package com.sefford.artdrian.usecases

import com.karumi.kotlinsnapshot.matchWithSnapshot
import com.sefford.artdrian.MetadataMother
import com.sefford.artdrian.datasources.FakeWallpaperApi
import com.sefford.artdrian.datasources.WallpaperMemoryDataSource
import com.sefford.artdrian.datasources.WallpaperRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class GetWallpaperTest {

    private lateinit var useCase: GetWallpaper
    private lateinit var local: WallpaperMemoryDataSource
    @BeforeEach
    fun setUp() {
        local = WallpaperMemoryDataSource()
        useCase = GetWallpaper(WallpaperRepository(FakeWallpaperApi{ listOf(MetadataMother.FIRST_METADATA)}, local))
    }

    @Test
    fun `retrieves a wallpaper from the UI`() {
        runBlocking {
            useCase.getWallpaper(MetadataMother.FIRST_METADATA_ID).matchWithSnapshot()
        }
    }

    @Test
    fun `returns a not found error if the ID does not exist`() {
        runBlocking {
            useCase.getWallpaper(MetadataMother.SECOND_METADATA_ID).matchWithSnapshot()
        }
    }
}
