package com.sefford.artdrian.wallpaperdetail.ui

import android.Manifest
import android.os.Build
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.QuestionMark
import androidx.compose.material.icons.sharp.Download
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.sefford.artdrian.R
import com.sefford.artdrian.model.Wallpaper
import com.sefford.artdrian.ui.theme.*
import com.sefford.artdrian.utils.isAtLeastAPI
import com.sefford.artdrian.wallpaperdetail.ui.WallpaperDetailViewModel.ViewState
import java.text.SimpleDateFormat
import java.util.*

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
    wallpaper: Wallpaper,
    onSaveClicked: () -> Unit = {},
    onApplyClicked: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(8.dp))
    ) {
        AsyncImage(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize(),
            model = wallpaper.mobile,
            contentDescription = wallpaper.name,
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .fillMaxHeight(0.2f)
                .background(Brush.verticalGradient(listOf(Color.Transparent, Black40)))
                .clip(RoundedCornerShape(8.dp)),
        )
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            ButtonWithLabel(
                icon = Icons.Rounded.Info,
                buttonText = R.string.detail_info_button,
                buttonColor = White20,
                iconTint = Color.White
            ) {}
            ButtonWithLabel(
                icon = Icons.Sharp.Download,
                buttonText = R.string.detail_save_button,
                buttonColor = White20,
                iconTint = Color.White,
                onClick = decorateWithPermissions(onSaveClicked)
            )
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
            Icon(icon, modifier = Modifier.size(48.dp), contentDescription = stringResource(buttonText), tint = iconTint)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = stringResource(buttonText), color = White50)
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
            Wallpaper(
                com.sefford.artdrian.model.Metadata(
                    id = "6",
                    views = 123,
                    downloads = 456,
                    slug = "ghost_waves_001",
                    created = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse("2022-11-05")!!,
                    updated = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse("2022-11-05")!!,
                )
            )
        ), "Example"
    )
}

@Preview
@Composable
private fun showError() {
    WallpaperDetailScreen(ViewState.NotFound("6"), "Example")
}
