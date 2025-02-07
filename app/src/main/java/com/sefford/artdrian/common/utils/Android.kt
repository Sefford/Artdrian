package com.sefford.artdrian.common.utils

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.pm.ApplicationInfo
import android.os.Build
import android.provider.MediaStore
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.work.CoroutineWorker
import com.sefford.artdrian.common.di.ApplicationComponent
import com.sefford.artdrian.common.di.TopComponentHolder

val ComponentActivity.graph: ApplicationComponent
    get() = (this.application as TopComponentHolder).graph!!

val CoroutineWorker.graph: ApplicationComponent
    get() = (applicationContext as TopComponentHolder).graph!!

fun Color.toHex(): String = Integer.toHexString(this.toArgb())

fun isAtLeastAPI(apiLevel: Int): Boolean {
    return Build.VERSION.SDK_INT >= apiLevel
}

fun ContentResolver.getUriFromPath(displayName: String): Boolean {
    val photoId: Long
    val photoUri = MediaStore.Images.Media.getContentUri("external")
    val projection = arrayOf(MediaStore.Images.ImageColumns._ID)
    val cursor = query(
        photoUri,
        projection,
        MediaStore.Images.ImageColumns.DISPLAY_NAME + " LIKE ?",
        arrayOf(displayName),
        null
    )!!
    val size = cursor.count
    cursor.close()
    return size > 0
}

val ApplicationInfo.debuggable: Boolean
    get() = (flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0


@SuppressLint("ComposableNaming")
@Composable
fun Window.enableFullscreen() {
    LaunchedEffect(true) {
        val controller = WindowCompat.getInsetsController(this@enableFullscreen, this@enableFullscreen.decorView)
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
}

@SuppressLint("ComposableNaming")
@Composable
fun Window.disableFullscreen() {
    LaunchedEffect(true) {
        val controller = WindowCompat.getInsetsController(this@disableFullscreen, this@disableFullscreen.decorView)
        controller.show(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
    }
}
