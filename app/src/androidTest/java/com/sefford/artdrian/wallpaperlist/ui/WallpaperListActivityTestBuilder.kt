package com.sefford.artdrian.wallpaperlist.ui

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.karumi.shot.ScreenshotTest
import com.sefford.artdrian.WallpaperMother
import com.sefford.artdrian.wallpapers.ui.list.WallpaperListActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WallpaperListActivityTestBuilder: ScreenshotTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<WallpaperListActivity>()

    @Test
    fun navigatesToTheWallpaperDetail() {
        composeTestRule.onNodeWithTag(WallpaperMother.FIRST_ID).performClick()

        compareScreenshot(composeTestRule)
    }
}
