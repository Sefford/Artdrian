package com.sefford.artdrian.wallpapers.ui.detail

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.QuestionMark
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sefford.artdrian.R
import com.sefford.artdrian.common.ui.LightDarkPreview
import com.sefford.artdrian.common.ui.theme.ArtdrianTheme
import com.sefford.artdrian.common.utils.enableFullscreen
import com.sefford.artdrian.wallpapers.domain.model.Wallpaper
import com.sefford.artdrian.wallpapers.ui.detail.effects.WallpaperDetailsEffect
import com.sefford.artdrian.wallpapers.ui.detail.viewmodel.WallpaperDetailsState
import com.sefford.artdrian.wallpapers.ui.detail.viewmodel.WallpaperDetailsViewModel
import com.sefford.artdrian.wallpapers.ui.detail.views.ButtonRow
import com.sefford.artdrian.wallpapers.ui.detail.views.ContentMode
import com.sefford.artdrian.wallpapers.ui.detail.views.Gradient
import com.sefford.artdrian.wallpapers.ui.detail.views.InfoOverlay
import com.sefford.artdrian.wallpapers.ui.views.ImageRequest
import com.sefford.artdrian.wallpapers.ui.views.WallpaperImage
import com.sefford.artdrian.wallpapers.ui.views.WallpaperPalette

@Composable
fun WallpaperDetailScreen(viewModel: WallpaperDetailsViewModel) {
    (LocalActivity.current as ComponentActivity).window.enableFullscreen()
    WallpaperDetailScreen(state = viewModel.state.collectAsStateWithLifecycle().value)
}

@Composable
fun WallpaperDetailScreen(
    state: WallpaperDetailsState,
) {
    when (state) {
        WallpaperDetailsState.Loading -> Loading()
        is WallpaperDetailsState.Content -> Content(wallpaper = state.wallpaper)
        is WallpaperDetailsState.Error -> Error()
    }
}

@Composable
private fun Loading() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.Center),
            color = ArtdrianTheme.colors.onBackground
        )
    }
}

@Composable
private fun Content(
    wallpaper: WallpaperDetail,
) {
    val (mode, setMode) = remember { mutableStateOf(ContentMode.ACTIONS) }
    Box(
        modifier = Modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.BottomCenter
    ) {
        WallpaperImage(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize(),
            image = ImageRequest(
                wallpaper.url,
                wallpaper.name,
                placeholder = wallpaper.tint(),
                ContentScale.Crop
            )
        )
        Gradient(mode = mode, color = wallpaper.tint)
        InfoOverlay(mode = mode, wallpaper = wallpaper)
        ButtonRow(mode, wallpaper.tint(), wallpaper.onTint(), setMode, wallpaper.listeners)
    }
}

class WallpaperDetail(
    val url: String,
    val name: String,
    val downloads: Int,
    val year: Int,
    val tint: @Composable () -> Color,
    val onTint: @Composable () -> Color,
    val listeners: Listeners
) {
    constructor(wallpaper: Wallpaper, listeners: Listeners) : this(
        url = wallpaper.images.mobile,
        name = wallpaper.title,
        downloads = wallpaper.downloads,
        year = wallpaper.published.year,
        tint = { WallpaperPalette[wallpaper.slug].muted },
        onTint = { WallpaperPalette[wallpaper.slug].onMuted },
        listeners = listeners
    )
}

class Listeners(
    val onShareLink: () -> Unit = {},
    val onDownloadMobile: () -> Unit = {},
    val onDownloadDesktop: () -> Unit = {},
    val onApplyMobile: () -> Unit = {},
    val onApplyDesktop: () -> Unit = {}
) {

    companion object {
        operator fun invoke(wallpaper: Wallpaper, effects: (WallpaperDetailsEffect) -> Unit) =
            Listeners(
                onShareLink = { effects(WallpaperDetailsEffect.Share(wallpaper.id)) },
                onDownloadMobile = { effects(WallpaperDetailsEffect.Download(wallpaper.images.mobile)) },
                onDownloadDesktop = { effects(WallpaperDetailsEffect.Download(wallpaper.images.desktop)) },
                onApplyMobile = { effects(WallpaperDetailsEffect.Apply(wallpaper.images.mobile)) },
                onApplyDesktop = { effects(WallpaperDetailsEffect.Apply(wallpaper.images.desktop)) }
            )
    }
}

@Composable
private fun Error() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            Icons.Rounded.QuestionMark,
            modifier = Modifier.size(120.dp),
            contentDescription = "",
            tint = ArtdrianTheme.colors.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.network_error),
            textAlign = TextAlign.Center,
            color = ArtdrianTheme.colors.onBackground
        )
    }
}

@Composable
@LightDarkPreview
private fun PreviewLoading() {
    ArtdrianTheme {
        WallpaperDetailScreen(state = WallpaperDetailsState.Loading)
    }
}

@Composable
@LightDarkPreview
private fun PreviewContent() {
    ArtdrianTheme {
        WallpaperDetailScreen(
            state = WallpaperDetailsState.Content(
                wallpaper = WallpaperDetail(
                    url = "http://image.jpg",
                    name = "Ghost Waves #001",
                    year = 2022,
                    downloads = 924,
                    tint = { ArtdrianTheme.colors.inversePrimary },
                    onTint = { ArtdrianTheme.colors.primary },
                    listeners = Listeners(),
                )
            )
        )
    }
}

@Composable
@LightDarkPreview
private fun PreviewError() {
    ArtdrianTheme {
        WallpaperDetailScreen(state = WallpaperDetailsState.Error.NotFound)
    }
}
