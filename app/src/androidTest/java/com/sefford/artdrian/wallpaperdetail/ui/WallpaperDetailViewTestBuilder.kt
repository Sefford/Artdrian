package com.sefford.artdrian.wallpaperdetail.ui

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import com.karumi.shot.ScreenshotTest
import com.sefford.artdrian.R
import com.sefford.artdrian.WallpaperMother
import com.sefford.artdrian.wallpaperdetail.ui.WallpaperDetailViewModel.ViewState.Content
import com.sefford.artdrian.wallpaperdetail.ui.WallpaperDetailViewModel.ViewState.Loading
import org.junit.Rule
import org.junit.Test

class WallpaperDetailViewTestBuilder : ScreenshotTest {

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

    @Test
    fun rendersWallpaperDetailScreenInfo() {
        var context: Context? = null
        composeTestRule.setContent {
            context = LocalContext.current
            WallpaperDetailScreen(Content(WallpaperMother.WALLPAPER_LIST.first()), ANY_TITLE)
        }

        composeTestRule.onNodeWithContentDescription(
            context!!.getString(R.string.detail_info_button)
        ).performClick()

        compareScreenshot(composeTestRule)
    }


    private companion object {
        const val ANY_TITLE = "Test #001"
    }
}
