package com.sefford.artdrian.wallpaperdetail.ui

import com.karumi.kotlinsnapshot.matchWithSnapshot
import com.sefford.artdrian.di.CoreModule
import com.sefford.artdrian.di.DaggerTestComponent
import com.sefford.artdrian.di.DoublesModule
import com.sefford.artdrian.wallpaperdetail.di.WallpaperDetailModule
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

@OptIn(ExperimentalCoroutinesApi::class)
class WallpaperDetailViewModelTestBuilder : Files {

    lateinit var viewModel: WallpaperDetailViewModel
    lateinit var server: MockWebServer

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()
        viewModel = WallpaperDetailViewModel()
        initializeViewModel()
    }

    private fun initializeViewModel() {
        DaggerTestComponent.builder()
            .coreModule(CoreModule())
            .doublesModule(DoublesModule())
            .build()
            .plus(WallpaperDetailModule(WALLPAPER_ID))
            .inject(viewModel)
    }

    @org.junit.Test
    fun `returns a Content View after a successful retrieval from the repository`() = runTest {
        server.enqueue(
            MockResponse().setResponseCode(200)
                .setBody("com/sefford/artdrian/usecases/metadata_response.json".readResourceFromFile())
        )

        viewModel.getWallpaper().last().matchWithSnapshot()
    }

    @org.junit.Test
    fun `notifies an error occurred during the retrieval`() = runTest {
        server.enqueue(MockResponse().setSocketPolicy(SocketPolicy.DISCONNECT_AT_START))

        viewModel.getWallpaper().last().matchWithSnapshot()
    }

    @org.junit.Test
    fun `sets the loading spinner when attempting to load`() = runTest {
        server.enqueue(
            MockResponse().setResponseCode(200)
                .setBody("com/sefford/artdrian/usecases/metadata_response.json".readResourceFromFile())
        )

        viewModel.getWallpaper().first().matchWithSnapshot()
    }

    @org.junit.Test
    fun `re-requesting the view model info skips the loading`() = runTest {
        server.enqueue(
            MockResponse().setResponseCode(200)
                .setBody("com/sefford/artdrian/usecases/metadata_response.json".readResourceFromFile())
        )

        viewModel.getWallpaper().last()
        viewModel.getWallpaper().toList().matchWithSnapshot()
    }

    private companion object {
        const val WALLPAPER_ID = "5"
    }
}
