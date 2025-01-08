package com.sefford.artdrian.wallpaperlist.ui

import com.sefford.artdrian.wallpapers.ui.list.WallpaperListScreen
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.karumi.shot.ScreenshotTest
import com.sefford.artdrian.WallpaperMother
import com.sefford.artdrian.wallpapers.ui.list.WallpaperListViewModel
import com.sefford.artdrian.wallpapers.ui.list.WallpaperListViewModel.Errors.NetworkError
import com.sefford.artdrian.wallpapers.ui.list.WallpaperListViewModel.Errors.NotFoundError
import com.sefford.artdrian.wallpapers.ui.list.WallpaperListViewModel.ViewState.Error
import com.sefford.artdrian.wallpapers.ui.list.WallpaperListViewModel.ViewState.Loading
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WallpaperListViewTestBuilder: ScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

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

        compareScreenshot(composeTestRule)
    }

    @Test
    fun rendersWallpaperListScreenContent() {
        composeTestRule.setContent { WallpaperListScreen(WallpaperListViewModel.ViewState.Content(WallpaperMother.WALLPAPER_LIST)) }

        compareScreenshot(composeTestRule)
    }
}
