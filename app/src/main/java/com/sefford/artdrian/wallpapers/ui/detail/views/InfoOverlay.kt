package com.sefford.artdrian.wallpapers.ui.detail.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sefford.artdrian.R
import com.sefford.artdrian.common.ui.theme.ArtdrianTheme
import com.sefford.artdrian.wallpapers.ui.detail.WallpaperDetail

@Composable
fun InfoOverlay(
    modifier: Modifier = Modifier,
    mode: ContentMode,
    wallpaper: WallpaperDetail
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        AnimatedVisibility(
            visible = mode == ContentMode.INFO,
            enter = slideInVertically(tween(), initialOffsetY = { it / 2 }) + fadeIn(tween()),
            exit = slideOutVertically(targetOffsetY = { it / 2 }) + fadeOut()
        ) {
            Text(
                wallpaper.name,
                fontWeight = FontWeight.Bold,
                style = ArtdrianTheme.typography.headlineLarge,
                color = wallpaper.onTint()
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        AnimatedVisibility(
            visible = mode == ContentMode.INFO,
            enter = slideInVertically(
                tween(delayMillis = 100), initialOffsetY = { it / 2 }) + fadeIn(
                tween(delayMillis = 100)
            ),
            exit = slideOutVertically(targetOffsetY = { it / 2 }) + fadeOut()
        ) {
            Text(
                stringResource(id = R.string.detail_info_downloads, wallpaper.downloads),
                fontWeight = FontWeight.Bold,
                color = wallpaper.onTint()
            )
        }
        AnimatedVisibility(
            visible = mode == ContentMode.INFO,
            enter = slideInVertically(
                tween(delayMillis = 200), initialOffsetY = { it / 2 }) + fadeIn(
                tween(delayMillis = 200)
            ),
            exit = slideOutVertically(targetOffsetY = { it / 2 }) + fadeOut()
        ) {
            Text(
                stringResource(R.string.detail_info_copyright, wallpaper.year),
                fontWeight = FontWeight.Bold,
                style = ArtdrianTheme.typography.bodySmall,
                color = wallpaper.onTint()
            )
        }
    }
}
