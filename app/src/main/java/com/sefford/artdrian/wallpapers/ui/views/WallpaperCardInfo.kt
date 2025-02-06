package com.sefford.artdrian.wallpapers.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.sefford.artdrian.common.ui.theme.ArtdrianTheme

@Composable
fun BoxScope.WallpaperCardInfo(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier
            .clip(ShapeDefaults.Medium)
            .background(ArtdrianTheme.colors.background.copy(alpha = 0.5f))
            .padding(8.dp, 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        content = content
    )
}
