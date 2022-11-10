package com.sefford.artdrian.datasources

import com.karumi.kotlinsnapshot.matchWithSnapshot
import com.sefford.artdrian.MetadataMother.FIRST_METADATA
import com.sefford.artdrian.MetadataMother.SECOND_METADATA
import com.sefford.artdrian.datasources.WallpaperRepository.CachePolicy.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

@DisplayName("In a WallpaperRepositoryTest")
@OptIn(ExperimentalCoroutinesApi::class)
class WallpaperRepositoryTest {

    @Nested
    @DisplayName("when getting all the wallpapers")
    inner class GetWallpapersSuccessfully {

        private lateinit var repository: WallpaperRepository
        private lateinit var api: FakeWallpaperApi
        private lateinit var local: WallpaperMemoryDataSource

        @BeforeEach
        fun setUp() = runTest {
            api = FakeWallpaperApi { listOf(FIRST_METADATA) }
            local = WallpaperMemoryDataSource()
            local.saveMetadata(listOf(SECOND_METADATA))
            repository = WallpaperRepository(api, local)
        }

        @ParameterizedTest
        @EnumSource(names = ["OFFLINE", "PRIORITIZE_LOCAL"])
        fun `retreives the metadata from cache`(policy: WallpaperRepository.CachePolicy) = runTest {
            repository.getAllMetadata(policy).matchWithSnapshot("retrieves the metadata from cache when policy is ${policy}")
        }

        @ParameterizedTest
        @EnumSource(names = ["NETWORK_ONLY", "PRIORITIZE_NETWORK"])
        fun `retreives the metadata from network`(policy: WallpaperRepository.CachePolicy) = runTest {
            repository.getAllMetadata(policy).matchWithSnapshot("retrieves the metadata from cache when policy is ${policy}")
        }
    }

    @Nested
    @DisplayName("when getting all the wallpapers")
    inner class GetWallpapersOnErrors {

        @Test
        fun `empty local cache defaults to network on PRIORITIZE_LOCAL`() = runTest {
            val api = FakeWallpaperApi { listOf(FIRST_METADATA) }
            val repository = WallpaperRepository(api, WallpaperMemoryDataSource())

            repository.getAllMetadata(WallpaperRepository.CachePolicy.PRIORITIZE_LOCAL).matchWithSnapshot()
        }

        @Test
        fun `a network error defaults to existing information in cache with PRIORITIZE_NETWORK`() = runTest {
            val api = FakeWallpaperApi { throw java.lang.RuntimeException() }
            val local = WallpaperMemoryDataSource()
            val repository = WallpaperRepository(api, local)

            local.saveMetadata(listOf(SECOND_METADATA))

            repository.getAllMetadata(PRIORITIZE_NETWORK).matchWithSnapshot()
        }

        @Test
        fun `a network error will return a NetworkingError error with NETWORK_ONLY`() = runTest {
            val api = FakeWallpaperApi { throw java.lang.RuntimeException() }
            val repository = WallpaperRepository(api, WallpaperMemoryDataSource())

            repository.getAllMetadata(NETWORK_ONLY).matchWithSnapshot()
        }

        @Test
        fun `an empty cache will return a NotFound error with OFFLINE`() = runTest {
            val api = FakeWallpaperApi { throw java.lang.RuntimeException() }
            val repository = WallpaperRepository(api, WallpaperMemoryDataSource())

            repository.getAllMetadata(OFFLINE).matchWithSnapshot()
        }

    }

}
