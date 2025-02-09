package com.sefford.artdrian.wallpapers.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.sefford.artdrian.common.ui.LightDarkPreview
import com.sefford.artdrian.common.ui.theme.ArtdrianTheme
import com.sefford.artdrian.wallpapers.domain.model.Wallpaper
import com.sefford.artdrian.wallpapers.ui.list.effects.WallpaperListEffect
import dev.chrisbanes.haze.HazeDefaults
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource

@Composable
fun WallpaperCard(
    modifier: Modifier = Modifier,
    wallpaper: WallpaperCardState,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1.778f)
    ) {
        val hazeState = remember { HazeState() }
        val hazeStyle = HazeStyle(
            backgroundColor = Color.Transparent,
            tints = listOf(HazeTint(ArtdrianTheme.colors.background.copy(alpha = 0.5f))),
            blurRadius = 8.dp,
            noiseFactor = HazeDefaults.noiseFactor,
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .testTag(wallpaper.tag)
                .clickable(onClick = wallpaper.onClick)
        ) {
            WallpaperImage(
                modifier = Modifier
                    .fillMaxSize()
                    .hazeSource(hazeState),
                image = ImageRequest(
                    url = wallpaper.url,
                    description = wallpaper.name,
                    placeholder = wallpaper.color(),
                    scale = ContentScale.Crop
                )
            )
            if (wallpaper.tag.isNotEmpty()) {
                WallpaperCardInfo(
                    Modifier
                        .align(Alignment.BottomStart)
                        .padding(bottom = 8.dp, start = 8.dp)
                        .clip(ShapeDefaults.Medium)
                        .hazeEffect(state = hazeState, style = hazeStyle)
                ) {
                    Text(text = wallpaper.tag, style = ArtdrianTheme.typography.bodySmall)
                }
            }
            if (wallpaper.showStatistics) {
                WallpaperCardInfo(
                    Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 8.dp, end = 8.dp)
                        .clip(ShapeDefaults.Medium)
                        .hazeEffect(state = hazeState, style = hazeStyle)
                ) {
                    Icon(Icons.Rounded.Download, modifier = Modifier.size(12.dp), contentDescription = "")
                    Text(text = wallpaper.downloads.toString(), style = ArtdrianTheme.typography.bodySmall)
                }
            }
        }
    }
}

sealed class WallpaperCardState(
    val name: String,
    val url: String,
    val tag: String,
    val downloads: Int,
    val color: @Composable () -> Color,
    val onClick: () -> Unit
) {

    data object Loading : WallpaperCardState(
        name = "",
        url = "",
        tag = "",
        color = { ArtdrianTheme.colors.outlineVariant.copy(alpha = .5f) },
        downloads = 0,
        onClick = {}
    )

    class Content(
        name: String,
        url: String,
        tag: String,
        color: @Composable () -> Color,
        downloads: Int,
        onClick: () -> Unit
    ) :
        WallpaperCardState(
            name = name,
            url = url,
            tag = tag,
            color = color,
            downloads = downloads,
            onClick = onClick
        )

    val showStatistics by lazy { downloads > 0 }

    companion object {
        operator fun invoke(wallpaper: Wallpaper, effect: (WallpaperListEffect) -> Unit) = Content(
            name = wallpaper.title,
            url = wallpaper.images.preview,
            tag = wallpaper.tags.first(),
            color = { WallpaperPalette[wallpaper.slug].dominant },
            downloads = wallpaper.downloads,
            onClick = {
                effect(WallpaperListEffect.GoToDetail(wallpaper.id, wallpaper.title))
            }
        )
    }
}

@Composable
@LightDarkPreview
fun LoadingCard() {
    ArtdrianTheme(darkMode = isSystemInDarkTheme()) {
        WallpaperCard(wallpaper = WallpaperCardState.Loading)
    }
}

@Composable
@LightDarkPreview
fun Content() {
    WallpaperCard(
        wallpaper = WallpaperCardState.Content(
            name = "Ghost Waves #001",
            url = "http://image.jpg",
            tag = "6K-READY",
            color = { ArtdrianTheme.colors.inversePrimary },
            downloads = 3105,
            onClick = { }
        )
    )
}
