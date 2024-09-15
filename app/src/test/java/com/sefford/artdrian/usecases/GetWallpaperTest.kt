package com.sefford.artdrian.usecases

import com.karumi.kotlinsnapshot.matchWithSnapshot
import com.sefford.artdrian.MetadataMother
import com.sefford.artdrian.datasources.FakeWallpaperApi
import com.sefford.artdrian.data.datasources.WallpaperMemoryDataSource
import com.sefford.artdrian.data.datasources.WallpaperRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetWallpaperTest {

    private lateinit var useCase: GetWallpaper
    private lateinit var local: WallpaperMemoryDataSource

    @BeforeEach
    fun setUp() {
        local = WallpaperMemoryDataSource()
        useCase = GetWallpaper(WallpaperRepository(FakeWallpaperApi { listOf(MetadataMother.FIRST_METADATA) }, local))
    }

    @Test
    fun `retrieves a wallpaper from the UI`() = runTest {
        useCase.getWallpaper(MetadataMother.FIRST_METADATA_ID).matchWithSnapshot()
    }

    @Test
    fun `returns a not found error if the ID does not exist`() = runTest {
        useCase.getWallpaper(MetadataMother.SECOND_METADATA_ID).matchWithSnapshot()
    }
}
