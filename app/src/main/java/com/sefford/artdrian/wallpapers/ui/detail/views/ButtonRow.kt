package com.sefford.artdrian.wallpapers.ui.detail.views

import android.Manifest
import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.sharp.Download
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.sefford.artdrian.R
import com.sefford.artdrian.common.ui.theme.ArtdrianTheme
import com.sefford.artdrian.common.utils.isAtLeastAPI
import com.sefford.artdrian.wallpapers.ui.detail.Listeners

@Composable
fun ButtonRow(
    mode: ContentMode,
    tint: Color,
    onTint: Color,
    setMode: (ContentMode) -> Unit,
    listeners: Listeners
) {
    Row(
        modifier = Modifier.Companion
            .padding(bottom = 16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        ButtonWithLabel(
            icon = when (mode) {
                ContentMode.ACTIONS -> Icons.Rounded.Info
                ContentMode.INFO -> Icons.Default.Close
            },
            buttonText = when (mode) {
                ContentMode.ACTIONS -> R.string.detail_info_button
                ContentMode.INFO -> R.string.detail_close_button
            },
            buttonColor = onTint,
            iconTint = tint,
            textColor = onTint
        ) {
            setMode(if (mode == ContentMode.ACTIONS) ContentMode.INFO else ContentMode.ACTIONS)
        }
        AnimatedButtonWithLabel(mode) {
            ButtonWithLabel(
                icon = Icons.Sharp.Download,
                buttonText = R.string.detail_save_button,
                buttonColor = onTint,
                iconTint = tint,
                textColor = onTint,
                onClick = decorateWithPermissions(listeners.onDownloadMobile)
            )
        }
        AnimatedButtonWithLabel(mode) {
            ButtonWithLabel(
                icon = Icons.Default.Wallpaper,
                buttonText = R.string.detail_apply_button,
                buttonColor = tint,
                iconTint = onTint,
                textColor = onTint,
                onClick = decorateWithPermissions(listeners.onApplyMobile),
            )
        }
    }
}

@Composable
@OptIn(ExperimentalPermissionsApi::class)
private fun decorateWithPermissions(onPermissionsGranted: () -> Unit): () -> Unit {
    val permissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    ) { permissions ->
        if (permissions.all { (_, status) -> status }) {
            onPermissionsGranted()
        }
    }
    return {
        if (permissionsState.allPermissionsGranted || isAtLeastAPI(Build.VERSION_CODES.Q)) {
            onPermissionsGranted()
        } else {
            permissionsState.launchMultiplePermissionRequest()
        }
    }
}
