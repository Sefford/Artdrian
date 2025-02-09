package com.sefford.artdrian.wallpapers.ui.detail.views

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.sefford.artdrian.R

enum class ContentMode {
    ACTIONS {
        @Composable
        override fun label(): String = stringResource(R.string.detail_info_button)
        override fun icon(): ImageVector = Icons.Rounded.Info
    },
    INFO {
        @Composable
        override fun label(): String = stringResource(R.string.detail_close_button)

        override fun icon(): ImageVector = Icons.Default.Close
    };

    @Composable
    abstract fun label(): String

    abstract fun icon(): ImageVector
}
