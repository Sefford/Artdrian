package com.sefford.artdrian.common.utils

import android.content.ContentResolver
import android.content.pm.ApplicationInfo
import android.os.Build
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.sefford.artdrian.TopComponentHolder
import com.sefford.artdrian.common.di.ApplicationComponent

val ComponentActivity.graph: ApplicationComponent
    get() = (this.application as TopComponentHolder).graph!!

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
