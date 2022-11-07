package com.sefford.artdrian.wallpaperlist.ui

import WallpaperListScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.QuestionMark
import androidx.compose.material.icons.rounded.WifiOff
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.sefford.artdrian.R
import com.sefford.artdrian.model.Wallpaper
import com.sefford.artdrian.ui.theme.ArtdrianTheme
import com.sefford.artdrian.utils.graph
import com.sefford.artdrian.wallpaperlist.ui.WallpaperListViewModel.Errors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class WallpaperListActivity : ComponentActivity() {

    private val viewModel: WallpaperListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        graph.inject(viewModel)
        requestWallpapers()
    }

    private fun requestWallpapers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getWallpapers().flowOn(Dispatchers.IO).collect { response ->
                    setContent {
                        WallpaperListScreen(response)
                    }
                }
            }
        }
    }
}
