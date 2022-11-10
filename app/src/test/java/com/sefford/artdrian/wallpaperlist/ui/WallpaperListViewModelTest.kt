package com.sefford.artdrian.wallpaperlist.ui

import com.karumi.kotlinsnapshot.matchWithSnapshot
import com.sefford.artdrian.di.CoreModule
import com.sefford.artdrian.di.DaggerTestComponent
import com.sefford.artdrian.di.DoublesModule
import com.sefford.utils.Files
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
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
            .doublesModule(DoublesModule())
            .build()
            .inject(viewModel)
    }

    @Test
    fun `returns a Content View after a successful retrieval from the repository`() = runTest {
        server.enqueue(
            MockResponse().setResponseCode(200)
                .setBody("com/sefford/artdrian/usecases/metadata_response.json".readResourceFromFile())
        )

        viewModel.getWallpapers().last().matchWithSnapshot()
    }

    @Test
    fun `notifies an error occurred during the retrieval`() = runTest {
        server.enqueue(MockResponse().setSocketPolicy(SocketPolicy.DISCONNECT_AT_START))

        viewModel.getWallpapers().last().matchWithSnapshot()
    }

    @Test
    fun `sets the loading spinner when attempting to load`() = runTest {
        server.enqueue(
            MockResponse().setResponseCode(200)
                .setBody("com/sefford/artdrian/usecases/metadata_response.json".readResourceFromFile())
        )

        viewModel.getWallpapers().first().matchWithSnapshot()
    }

    @Test
    fun `re-requesting the view model info skips the loading`() = runTest {
        server.enqueue(
            MockResponse().setResponseCode(200)
                .setBody("com/sefford/artdrian/usecases/metadata_response.json".readResourceFromFile())
        )

        viewModel.getWallpapers().last()
        viewModel.getWallpapers().toList().matchWithSnapshot()
    }
}
