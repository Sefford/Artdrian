package com.sefford.artdrian.wallpaperdetail.ui

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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.sefford.artdrian.R
import com.sefford.artdrian.model.Metadata
import com.sefford.artdrian.ui.theme.ArtdrianTheme
import com.sefford.artdrian.ui.theme.Black40
import com.sefford.artdrian.ui.theme.Black80
import com.sefford.artdrian.ui.theme.Typography
import com.sefford.artdrian.ui.theme.White20
import com.sefford.artdrian.ui.theme.White50
import com.sefford.artdrian.utils.isAtLeastAPI
import com.sefford.artdrian.wallpaperdetail.ui.ContentMode.ACTIONS
import com.sefford.artdrian.wallpaperdetail.ui.ContentMode.INFO
import com.sefford.artdrian.wallpaperdetail.ui.WallpaperDetailViewModel.ViewState
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WallpaperDetailScreen(
    viewState: ViewState,
    name: String,
    onSaveClicked: () -> Unit = {},
    onApplyClicked: () -> Unit = {}
) {
    ArtdrianTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = name) },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Black,
                        titleContentColor = Color.White
                    )
                )
            },
            content = { innerPadding ->
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                ) {
                    when (viewState) {
                        ViewState.Loading -> ShowLoading()
                        is ViewState.Content -> ShowWallpaper(
                            wallpaper = viewState.wallpaper,
                            onSaveClicked = onSaveClicked,
                            onApplyClicked = onApplyClicked
                        )

                        is ViewState.NotFound -> ShowError()
                    }
                }
            })
    }
}

@Composable
private fun ShowLoading() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.Center), color = Color.Black
        )
    }
}

@Composable
private fun ShowWallpaper(
    wallpaper: Metadata,
    onSaveClicked: () -> Unit = {},
    onApplyClicked: () -> Unit = {}
) {
    val (mode, setMode) = remember { mutableStateOf(ACTIONS) }
    Box(
        modifier = Modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.BottomCenter
    ) {
        AsyncImage(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize(),
            model = wallpaper.mobile,
            contentDescription = wallpaper.name,
            contentScale = ContentScale.Crop
        )
        val gradientOrigin: Float by animateFloatAsState(
            when (mode) {
                ACTIONS -> 0.8f
                INFO -> 0f
            }
        )
        InfoOverlay(gradientOrigin, mode, wallpaper)
        ButtonRow(mode, setMode, onSaveClicked, onApplyClicked)
    }
}

@Composable
private fun InfoOverlay(
    gradientOrigin: Float,
    mode: ContentMode,
    wallpaper: Metadata
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(
                Brush.verticalGradient(
                    gradientOrigin to Color.Transparent,
                    1f to Black40
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
            Text(wallpaper.name, fontWeight = FontWeight.Bold, style = Typography.headlineLarge)
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
            Text(stringResource(R.string.detail_info_copyright), fontWeight = FontWeight.Bold, style = Typography.bodySmall)
        }
    }
}

@Composable
private fun ButtonRow(
    mode: ContentMode,
    setMode: (ContentMode) -> Unit,
    onSaveClicked: () -> Unit,
    onApplyClicked: () -> Unit
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
            buttonColor = White20,
            iconTint = Color.White
        ) {
            setMode(if (mode == ACTIONS) INFO else ACTIONS)
        }
        AnimatedButtonWithLabel(mode) {
            ButtonWithLabel(
                icon = Icons.Sharp.Download,
                buttonText = R.string.detail_save_button,
                buttonColor = White20,
                iconTint = Color.White,
                onClick = decorateWithPermissions(onSaveClicked)
            )
        }
        AnimatedButtonWithLabel(mode) {
            ButtonWithLabel(
                icon = Icons.Default.Wallpaper,
                buttonText = R.string.detail_apply_button,
                buttonColor = Black80,
                iconTint = Color.White,
                onClick = decorateWithPermissions(onApplyClicked)
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

private enum class ContentMode {
    ACTIONS,
    INFO
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
    iconTint: Color = White50,
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
        ) { text -> Text(text = stringResource(text), color = White50) }
    }
}

@Composable
private fun ShowError() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(Icons.Rounded.QuestionMark, modifier = Modifier.size(120.dp), contentDescription = "", tint = Color.Black)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.network_error),
            textAlign = TextAlign.Center,
            color = Color.Black
        )
    }
}

@Preview
@Composable
private fun showLoading() {
    WallpaperDetailScreen(ViewState.Loading, "Example")
}

@Preview
@Composable
private fun showContent() {
    WallpaperDetailScreen(
        ViewState.Content(
            Metadata(
                id = "6",
                title = "ghost_waves_001",
                views = 123,
                downloads = 456,
                slug = "ghost_waves_001",
                created = Clock.System.now().toLocalDateTime(TimeZone.UTC),
            )
        ), "Example"
    )
}

@Preview
@Composable
private fun showError() {
    WallpaperDetailScreen(ViewState.NotFound("6"), "Example")
}
