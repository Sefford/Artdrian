package com.sefford.artdrian.datasources

import com.karumi.kotlinsnapshot.matchWithSnapshot
import com.sefford.artdrian.MetadataMother
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class WallpaperMemoryDataSourceTest {

    private lateinit var dataSource: WallpaperMemoryDataSource
    private lateinit var api: FakeWallpaperApi

    @BeforeEach
    fun setUp() {
        dataSource = WallpaperMemoryDataSource()
    }

    @Test
    fun `saves and retrieves metadata`() {
        runBlocking {
            dataSource.saveMetadata(MetadataMother.EXAMPLE_METADATA)
            dataSource.getAllMetadata().matchWithSnapshot()
        }
    }

    @Test
    fun `an empty dataSource returns a NotFound error`() {
        runBlocking {
            dataSource.getAllMetadata().matchWithSnapshot()
        }
    }

    @Test
    fun `querying an existing metadata ID returns it`() {
        runBlocking {
            dataSource.saveMetadata(MetadataMother.EXAMPLE_METADATA)
            dataSource.getWallpaperMetadata(MetadataMother.FIRST_METADATA_ID).matchWithSnapshot()
        }
    }

    @Test
    fun `querying an not existing metadata ID returns a NotFound error`() {
        runBlocking {
            dataSource.saveMetadata(MetadataMother.EXAMPLE_METADATA)
            dataSource.getWallpaperMetadata(Int.MAX_VALUE.toString()).matchWithSnapshot()
        }
    }
}
