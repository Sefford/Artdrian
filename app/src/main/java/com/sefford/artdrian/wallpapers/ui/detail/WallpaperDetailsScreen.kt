package com.sefford.artdrian.wallpapers.ui.detail

import android.Manifest
import android.os.Build
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.QuestionMark
import androidx.compose.material.icons.sharp.Download
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.sefford.artdrian.R
import com.sefford.artdrian.common.ui.LightDarkPreview
import com.sefford.artdrian.common.ui.theme.ArtdrianTheme
import com.sefford.artdrian.common.utils.isAtLeastAPI
import com.sefford.artdrian.wallpapers.domain.model.Wallpaper
import com.sefford.artdrian.wallpapers.ui.detail.ContentMode.ACTIONS
import com.sefford.artdrian.wallpapers.ui.detail.ContentMode.INFO
import com.sefford.artdrian.wallpapers.ui.detail.effects.WallpaperDetailsEffect
import com.sefford.artdrian.wallpapers.ui.detail.viewmodel.WallpaperDetailsState
import com.sefford.artdrian.wallpapers.ui.views.ImageRequest
import com.sefford.artdrian.wallpapers.ui.views.WallpaperImage
import com.sefford.artdrian.wallpapers.ui.views.WallpaperPalette

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
    val (mode, setMode) = remember { mutableStateOf(ACTIONS) }
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
                placeholder = wallpaper.color(),
                ContentScale.Crop
            )
        )
        val gradientOrigin: Float by animateFloatAsState(
            when (mode) {
                ACTIONS -> 0.8f
                INFO -> 0f
            }
        )
        InfoOverlay(gradientOrigin, mode, wallpaper)
        ButtonRow(mode, setMode, wallpaper.listeners)
    }
}

@Composable
private fun InfoOverlay(
    gradientOrigin: Float,
    mode: ContentMode,
    wallpaper: WallpaperDetail
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(
                Brush.verticalGradient(
                    gradientOrigin to Color.Transparent,
                    1f to ArtdrianTheme.colors.secondary
                )
            )
            .padding(start = 16.dp)
            .clip(RoundedCornerShape(8.dp)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        AnimatedVisibility(
            visible = mode == INFO,
            enter = slideInVertically(tween(), initialOffsetY = { it / 2 }) + fadeIn(tween()),
            exit = slideOutVertically(targetOffsetY = { it / 2 }) + fadeOut()
        ) {
            Text(wallpaper.name, fontWeight = FontWeight.Bold, style = ArtdrianTheme.typography.headlineLarge)
        }
        Spacer(modifier = Modifier.height(24.dp))
        AnimatedVisibility(
            visible = mode == INFO,
            enter = slideInVertically(
                tween(delayMillis = 100), initialOffsetY = { it / 2 }) + fadeIn(
                tween(delayMillis = 100)
            ),
            exit = slideOutVertically(targetOffsetY = { it / 2 }) + fadeOut()
        ) {
            Text(
                stringResource(id = R.string.detail_info_downloads, wallpaper.downloads),
                fontWeight = FontWeight.Bold
            )
        }
        AnimatedVisibility(
            visible = mode == INFO,
            enter = slideInVertically(
                tween(delayMillis = 200), initialOffsetY = { it / 2 }) + fadeIn(
                tween(delayMillis = 200)
            ),
            exit = slideOutVertically(targetOffsetY = { it / 2 }) + fadeOut()
        ) {
            Text(
                stringResource(R.string.detail_info_copyright),
                fontWeight = FontWeight.Bold,
                style = ArtdrianTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun ButtonRow(
    mode: ContentMode,
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
                ACTIONS -> Icons.Rounded.Info
                INFO -> Icons.Default.Close
            },
            buttonText = when (mode) {
                ACTIONS -> R.string.detail_info_button
                INFO -> R.string.detail_close_button
            },
            buttonColor = ArtdrianTheme.colors.surface,
            iconTint = ArtdrianTheme.colors.onSurface
        ) {
            setMode(if (mode == ACTIONS) INFO else ACTIONS)
        }
        AnimatedButtonWithLabel(mode) {
            ButtonWithLabel(
                icon = Icons.Sharp.Download,
                buttonText = R.string.detail_save_button,
                buttonColor = ArtdrianTheme.colors.surface,
                iconTint = ArtdrianTheme.colors.onSurface,
                onClick = decorateWithPermissions(listeners.onDownloadMobile)
            )
        }
        AnimatedButtonWithLabel(mode) {
            ButtonWithLabel(
                icon = Icons.Default.Wallpaper,
                buttonText = R.string.detail_apply_button,
                buttonColor = ArtdrianTheme.colors.inverseSurface,
                iconTint = ArtdrianTheme.colors.inverseOnSurface,
                onClick = decorateWithPermissions(listeners.onApplyMobile)
            )
        }
    }
}

@Composable
@OptIn(ExperimentalAnimationApi::class)
private fun AnimatedButtonWithLabel(
    mode: ContentMode,
    content: @Composable () -> Unit,
) {
    AnimatedContent(
        targetState = mode,
        transitionSpec = { scaleIn() with scaleOut() }
    ) { animatedMode ->
        when (animatedMode) {
            ACTIONS -> content()
            INFO -> Spacer(modifier = Modifier.width(64.dp))
        }
    }
}

class WallpaperDetail(
    val url: String,
    val name: String,
    val downloads: Int,
    val color: @Composable () -> Color,
    val listeners: Listeners
) {
    constructor(wallpaper: Wallpaper, listeners: Listeners) : this(
        url = wallpaper.images.mobile,
        name = wallpaper.title,
        downloads = wallpaper.downloads,
        color = { WallpaperPalette[wallpaper.id].dominant },
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

private enum class ContentMode {
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

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun ButtonWithLabel(
    icon: ImageVector,
    @StringRes buttonText: Int,
    buttonColor: Color,
    iconTint: Color = Color.White,
    onClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(
            modifier = Modifier
                .size(64.dp),
            shape = CircleShape,
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
            contentPadding = PaddingValues(16.dp)
        ) {
            AnimatedContent(
                targetState = icon,
                transitionSpec = { scaleIn() + fadeIn() with scaleOut() + fadeOut() }
            ) { buttonIcon ->
                Icon(
                    buttonIcon,
                    modifier = Modifier.size(48.dp),
                    contentDescription = stringResource(buttonText),
                    tint = iconTint
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        AnimatedContent(
            targetState = buttonText,
            transitionSpec = { scaleIn() + fadeIn() with scaleOut() + fadeOut() }
        ) { text -> Text(text = stringResource(text), color = Color.White) }
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
                    downloads = 924,
                    color = { ArtdrianTheme.colors.inversePrimary },
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
