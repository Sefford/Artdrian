package com.sefford.artdrian.wallpaperdetail.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Environment
import android.util.Log
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.espresso.idling.CountingIdlingResource
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.karumi.shot.ScreenshotTest
import com.sefford.artdrian.R
import com.sefford.artdrian.WallpaperMother
import com.sefford.artdrian.test.idlingresources.CountdownIdlingResource
import com.sefford.artdrian.test.idlingresources.FileIdlingResource
import com.sefford.artdrian.wallpaperlist.ui.WallpaperListActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch

@RunWith(AndroidJUnit4::class)
class WallpaperDetailActivityTestBuilder : ScreenshotTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<WallpaperListActivity>()

    @Test
    fun savesTheWallpaper() {
        composeTestRule.onNodeWithTag(WallpaperMother.FIRST_ID).performClick()

        composeTestRule.onNodeWithContentDescription(
            composeTestRule.activity.getString(R.string.detail_save_button)
        ).performClick()

        val file = Environment.getExternalStorageDirectory().absolutePath +
                "/" + Environment.DIRECTORY_PICTURES + "/Wallpapers/Artdrian/ghost_waves_001.png"
        val idlingResource = FileIdlingResource(file)
        composeTestRule.registerIdlingResource(idlingResource)
    }

    @Test
    fun appliesTheWallpaper() {
        val idlingResource = CountdownIdlingResource(1)

        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                idlingResource.decrement()
            }
        }

        composeTestRule.activity.application.registerReceiver(receiver, IntentFilter(Intent.ACTION_WALLPAPER_CHANGED))

        composeTestRule.onNodeWithTag(WallpaperMother.FIRST_ID).performClick()

        composeTestRule.onNodeWithContentDescription(
            composeTestRule.activity.getString(R.string.detail_apply_button)
        ).performClick()
        composeTestRule.registerIdlingResource(idlingResource)
    }

}
