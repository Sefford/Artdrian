package com.sefford.artdrian.datasources

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.DisplayName

@DisplayName("In a WallpaperRepositoryTest")
@OptIn(ExperimentalCoroutinesApi::class)
class WallpaperRepositoryTest {
//
//    @Nested
//    @DisplayName("when getting all the wallpapers")
//    inner class GetWallpapersSuccessfully {
//
//        private lateinit var repository: WallpaperRepository
//        private lateinit var api: FakeWallpaperApi
//        private lateinit var local: WallpaperMemoryDataSource
//        @BeforeEach
//        fun setUp() = runTest {
//            api = FakeWallpaperApi { WallpaperResponse(listOf(FIRST_METADATA_DTO)) }
//            local = WallpaperMemoryDataSource(scope = TestScope()).also { it.saveMetadata(listOf(SECOND_METADATA)) }
//            repository = WallpaperRepository(api, local)
//        }
//
//        @ParameterizedTest
//        @EnumSource(names = ["OFFLINE", "PRIORITIZE_LOCAL"])
//        fun `retreives the metadata from cache`(policy: CachePolicy) = runTest {
//            repository.getAllMetadata(policy).matchWithSnapshot("retrieves the metadata from cache when policy is ${policy}")
//        }
//
//        @ParameterizedTest
//        @EnumSource(names = ["NETWORK_ONLY", "PRIORITIZE_NETWORK"])
//        fun `retreives the metadata from network`(policy: CachePolicy) = runTest {
//            repository.getAllMetadata(policy).matchWithSnapshot("retrieves the metadata from cache when policy is ${policy}")
//        }
//    }
//
//    @Nested
//    @DisplayName("when getting all the wallpapers")
//    inner class GetWallpapersOnErrors {
//        private val local =  WallpaperMemoryDataSource(scope = TestScope())
//
//        @Test
//        fun `empty local cache defaults to network on PRIORITIZE_LOCAL`() = runTest {
//            val api = FakeWallpaperApi { WallpaperResponse(listOf(FIRST_METADATA_DTO)) }
//            val repository = WallpaperRepository(api, local)
//
//            repository.getAllMetadata(CachePolicy.PRIORITIZE_LOCAL).matchWithSnapshot()
//        }
//
//        @Test
//        fun `a network error defaults to existing information in cache with PRIORITIZE_NETWORK`() = runTest {
//            val api = FakeWallpaperApi { throw java.lang.RuntimeException() }
//            val repository = WallpaperRepository(api, local)
//
//            local.saveMetadata(listOf(SECOND_METADATA))
//
//            repository.getAllMetadata(CachePolicy.PRIORITIZE_NETWORK).matchWithSnapshot()
//        }
//
//        @Test
//        fun `a network error will return a NetworkingError error with NETWORK_ONLY`() {
//            runTest {
//                val api = FakeWallpaperApi { throw java.lang.RuntimeException() }
//                val repository = WallpaperRepository(api, local)
//
//                repository.getAllMetadata(CachePolicy.NETWORK_ONLY).matchWithSnapshot()
//            }
//        }
//
//        @Test
//        fun `an empty cache will return a NotFound error with OFFLINE`() = runTest {
//            val api = FakeWallpaperApi { throw java.lang.RuntimeException() }
//            val repository = WallpaperRepository(api, local)
//
//            repository.getAllMetadata(CachePolicy.OFFLINE).matchWithSnapshot()
//        }
//    }

}
