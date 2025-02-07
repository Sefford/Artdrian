package com.sefford.artdrian.wallpapers.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.QuestionMark
import androidx.compose.material.icons.rounded.WifiOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sefford.artdrian.R
import com.sefford.artdrian.common.ui.LightDarkPreview
import com.sefford.artdrian.common.ui.theme.ArtdrianTheme
import com.sefford.artdrian.wallpapers.ui.list.viewmodel.WallpaperListState
import com.sefford.artdrian.wallpapers.ui.views.WallpaperCard
import com.sefford.artdrian.wallpapers.ui.views.WallpaperCardState
import com.sefford.artdrian.wallpapers.ui.views.WallpaperPalette

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun WallpaperListScreen(response: WallpaperListState) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = ArtdrianTheme.colors.background,
                    titleContentColor = ArtdrianTheme.colors.onBackground
                )
            )
        },
        content = { innerPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                when (response) {
                    WallpaperListState.Loading -> Content(response.wallpapers)
                    is WallpaperListState.Content -> Content(response.wallpapers)
                    is WallpaperListState.Errors -> Error(response)
                }
            }
        })
}

@Composable
private fun Content(wallpapers: List<WallpaperCardState>) {
    LazyColumn(
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(wallpapers) { wallpaper -> WallpaperCard(wallpaper = wallpaper) }
    }
}

@Composable
private fun Error(errors: WallpaperListState.Errors) {
    val icon = when (errors) {
        WallpaperListState.Errors.Critical -> Icons.Rounded.WifiOff
        WallpaperListState.Errors.Empty -> Icons.Rounded.QuestionMark
        WallpaperListState.Errors.Network -> Icons.Rounded.QuestionMark
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(icon, modifier = Modifier.size(120.dp), contentDescription = "", tint = ArtdrianTheme.colors.onBackground)
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
        WallpaperListScreen(WallpaperListState.Loading)
    }
}

@Composable
@LightDarkPreview
private fun PreviewContent() {
    ArtdrianTheme {
        WallpaperListScreen(WallpaperListState.Content(List(5) { index ->
            WallpaperCardState.Content(
                name = "Ghost Waves #00$index",
                url = "http://image.jpg",
                tag = "${index}K-READY",
                color = { WallpaperPalette[index].dominant },
                downloads = 92 * index,
                onClick = {}
            )
        }))
    }
}

@Composable
@LightDarkPreview
private fun PreviewError() {
    ArtdrianTheme { WallpaperListScreen(WallpaperListState.Errors.Network) }
}
