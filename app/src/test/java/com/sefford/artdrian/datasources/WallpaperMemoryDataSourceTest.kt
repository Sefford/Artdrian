package com.sefford.artdrian.datasources

import com.karumi.kotlinsnapshot.matchWithSnapshot
import com.sefford.artdrian.MetadataMother
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WallpaperMemoryDataSourceTest {

    private lateinit var dataSource: WallpaperMemoryDataSource

    @BeforeEach
    fun setUp() {
        dataSource = WallpaperMemoryDataSource(scope = TestScope())
    }

    @Test
    fun `saves and retrieves metadata`() = runTest {
        dataSource.saveMetadata(MetadataMother.EXAMPLE_METADATA)
        dataSource.getAllMetadata().matchWithSnapshot()
    }

    @Test
    fun `an empty dataSource returns a NotFound error`() = runTest {
        dataSource.getAllMetadata().matchWithSnapshot()
    }

    @Test
    fun `querying an existing metadata ID returns it`() = runTest {
        dataSource.saveMetadata(MetadataMother.EXAMPLE_METADATA)
        dataSource.getWallpaperMetadata(MetadataMother.FIRST_METADATA_ID).matchWithSnapshot()
    }

    @Test
    fun `querying an not existing metadata ID returns a NotFound error`() = runTest {
        dataSource.saveMetadata(MetadataMother.EXAMPLE_METADATA)
        dataSource.getWallpaperMetadata(Int.MAX_VALUE.toString()).matchWithSnapshot()
    }
}
