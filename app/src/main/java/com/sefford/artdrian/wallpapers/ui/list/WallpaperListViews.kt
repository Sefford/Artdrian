package com.sefford.artdrian.wallpapers.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.QuestionMark
import androidx.compose.material.icons.rounded.WifiOff
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sefford.artdrian.R
import com.sefford.artdrian.common.ui.theme.ArtdrianTheme
import com.sefford.artdrian.wallpapers.domain.model.Wallpaper
import com.sefford.artdrian.wallpapers.ui.list.viewmodel.WallpaperListState
import com.sefford.artdrian.wallpapers.ui.views.WallpaperCard
import com.sefford.artdrian.wallpapers.ui.views.WallpaperCardState

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun WallpaperListScreen(response: WallpaperListState, onItemClicked: (String, String) -> Unit = { _, _ -> }) {
    ArtdrianTheme {
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
                        WallpaperListState.Loading -> ShowLoading()
                        is WallpaperListState.Content -> ShowWallpapers(response.wallpapers, onItemClicked)
                        is WallpaperListState.Errors -> ShowError(response)
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
                .align(Alignment.Center),
            color = ArtdrianTheme.colors.onBackground
        )
    }
}

@Composable
private fun ShowWallpapers(wallpapers: List<WallpaperCardState>, onItemClick: (String, String) -> Unit) {
    LazyColumn(
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(wallpapers) { wallpaper -> WallpaperCard(wallpaper = wallpaper) }
    }
}

@Composable
private fun ShowError(errors: WallpaperListState.Errors) {
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

@Preview
@Composable
private fun showLoading() {
    WallpaperListScreen(WallpaperListState.Loading)
}

@Preview
@Composable
private fun showContent() {
    WallpaperListScreen(WallpaperListState.Content(listOf()))
}

@Preview
@Composable
private fun showError() {
    WallpaperListScreen(WallpaperListState.Errors.Network)
}
