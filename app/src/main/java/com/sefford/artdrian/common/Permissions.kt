package com.sefford.artdrian.common

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat

class Permissions(
    private val context: Context
) {

    val notifications: Boolean
        get() = Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU || Manifest.permission.POST_NOTIFICATIONS.enabled()

    private fun String.enabled(): Boolean =
        ActivityCompat.checkSelfPermission(context, this) == PackageManager.PERMISSION_GRANTED
}
