package com.sefford.artdrian.wallpaperlist.ui

import WallpaperListScreen
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.karumi.shot.ScreenshotTest
import com.sefford.artdrian.WallpaperMother
import com.sefford.artdrian.wallpaperlist.ui.WallpaperListViewModel.Errors.NetworkError
import com.sefford.artdrian.wallpaperlist.ui.WallpaperListViewModel.Errors.NotFoundError
import com.sefford.artdrian.wallpaperlist.ui.WallpaperListViewModel.ViewState.Error
import com.sefford.artdrian.wallpaperlist.ui.WallpaperListViewModel.ViewState.Loading
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WallpaperListViewTest: ScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
    }

    @Test
    fun rendersWallpaperListScreenInLoadingState() {
        composeTestRule.setContent { WallpaperListScreen(Loading) }

        compareScreenshot(composeTestRule)
    }

    @Test
    fun rendersWallpaperListScreenInNetworkErrorState() {
        composeTestRule.setContent { WallpaperListScreen(Error(NetworkError(0))) }

        compareScreenshot(composeTestRule)
    }

    @Test
    fun rendersWallpaperListScreenInNotFoundErrorState() {
        composeTestRule.setContent { WallpaperListScreen(Error(NotFoundError(""))) }

        composeTestRule.onRoot().printToLog("Sefford")

        compareScreenshot(composeTestRule)
    }

    @Test
    fun rendersWallpaperListScreenContent() {
        composeTestRule.setContent { WallpaperListScreen(WallpaperListViewModel.ViewState.Content(WallpaperMother.WALLPAPER_LIST)) }

        compareScreenshot(composeTestRule)
    }
}
