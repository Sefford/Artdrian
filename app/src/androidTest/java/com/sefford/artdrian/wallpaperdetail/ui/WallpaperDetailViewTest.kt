package com.sefford.artdrian.wallpaperdetail.ui

import androidx.compose.ui.test.junit4.createComposeRule
import com.karumi.shot.ScreenshotTest
import com.sefford.artdrian.WallpaperMother
import com.sefford.artdrian.wallpaperdetail.ui.WallpaperDetailViewModel.ViewState.*
import org.junit.Rule
import org.junit.Test

internal class WallpaperDetailViewTest : ScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()


    @Test
    fun rendersWallpaperDetailScreenInLoadingState() {
        composeTestRule.setContent { WallpaperDetailScreen(Loading, ANY_TITLE) }

        compareScreenshot(composeTestRule)
    }

    @Test
    fun rendersWallpaperDetailScreenInNotFoundErrorState() {
        composeTestRule.setContent { WallpaperDetailScreen(Loading, ANY_TITLE) }

        compareScreenshot(composeTestRule)
    }

    @Test
    fun rendersWallpaperDetailScreenContent() {
        composeTestRule.setContent { WallpaperDetailScreen(Content(WallpaperMother.WALLPAPER_LIST.first()), ANY_TITLE) }

        compareScreenshot(composeTestRule)
    }

    private companion object {
        const val ANY_TITLE = "Test #001"
    }
}
