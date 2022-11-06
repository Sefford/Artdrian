package com.sefford.artdrian.wallpaperlist.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.WifiOff
import androidx.compose.material.icons.rounded.WifiTetheringError
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.centerAlignedTopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sefford.artdrian.R
import com.sefford.artdrian.model.Wallpaper
import com.sefford.artdrian.ui.theme.ArtdrianTheme
import com.sefford.artdrian.utils.graph
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.runBlocking

class WallpaperListActivity : ComponentActivity() {

    private val viewModel: WallpaperListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        graph.inject(viewModel)
        runBlocking {
            viewModel.getWallpapers().flowOn(Dispatchers.IO).collect { response ->
                setContent {
                    SetContent(response)
                }
            }
        }
    }

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    private fun SetContent(response: WallpaperListViewModel.ViewState) {
        ArtdrianTheme {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(text = stringResource(id = R.string.app_name)) },
                        colors = centerAlignedTopAppBarColors(
                            containerColor = Color.Black,
                            titleContentColor = Color.White
                        )
                    )
                },
                content = { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding), color = MaterialTheme.colorScheme
                            .background
                    ) {
                        when (response) {
                            WallpaperListViewModel.ViewState.Loading -> ShowLoading()
                            is WallpaperListViewModel.ViewState.Content -> ShowWallpapers(response.wallpapers)
                            WallpaperListViewModel.ViewState.NetworkError -> ShowError()
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
    private fun ShowWallpapers(wallpapers: List<Wallpaper>) {
        LazyColumn(
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(wallpapers) { wallpaper ->
                WallpaperCard(wallpaper = wallpaper)
            }
        }
    }

    @Composable
    private fun ShowError() {
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(Icons.Rounded.WifiOff, modifier = Modifier.size(120.dp), contentDescription = "")
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = stringResource(R.string.network_error), textAlign = TextAlign.Center)
        }
    }

    @Preview
    @Composable
    fun showError() {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
           ShowError()
        }
    }
}
