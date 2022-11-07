package com.sefford.artdrian.wallpaperlist.ui

import com.karumi.kotlinsnapshot.matchWithSnapshot
import com.sefford.artdrian.di.CoreModule
import com.sefford.artdrian.di.DaggerTestComponent
import com.sefford.utils.Files
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class WallpaperListViewModelTest : Files {

    lateinit var viewModel: WallpaperListViewModel
    lateinit var server: MockWebServer

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()
        viewModel = WallpaperListViewModel()
        initializeViewModel()
    }

    private fun initializeViewModel() {
        DaggerTestComponent.builder()
            .coreModule(CoreModule(server.url("/").toString()))
            .build()
            .inject(viewModel)
    }

    @Test
    fun `returns a Content View after a successful retrieval from the repository`() {
        server.enqueue(
            MockResponse().setResponseCode(200)
                .setBody("com/sefford/artdrian/usecases/metadata_response.json".readResourceFromFile())
        )

        runBlocking {
            viewModel.getWallpapers().last().matchWithSnapshot()
        }
    }

    @Test
    fun `notifies an error occurred during the retrieval`() {
        server.enqueue(MockResponse().setSocketPolicy(SocketPolicy.DISCONNECT_AT_START))

        runBlocking {
            viewModel.getWallpapers().last().matchWithSnapshot()
        }
    }

    @Test
    fun `sets the loading spinner when attempting to load`() {
        server.enqueue(
            MockResponse().setResponseCode(200)
                .setBody("com/sefford/artdrian/usecases/metadata_response.json".readResourceFromFile())
        )

        runBlocking {
            viewModel.getWallpapers().first().matchWithSnapshot()
        }
    }

    @Test
    fun `re-requesting the view model info skips the loading`() {
        server.enqueue(
            MockResponse().setResponseCode(200)
                .setBody("com/sefford/artdrian/usecases/metadata_response.json".readResourceFromFile())
        )

        runBlocking {
            viewModel.getWallpapers().last()
            viewModel.getWallpapers().toList().matchWithSnapshot()
        }
    }
}
