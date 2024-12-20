package com.sefford.artdrian.wallpaperlist.ui

import WallpaperListScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.sefford.artdrian.ui.navigation.goToDetail
import com.sefford.artdrian.utils.graph
import kotlinx.coroutines.launch

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
                viewModel.getWallpapers().collect { response ->
                    setContent {
                        WallpaperListScreen(response) { wallpaperId, wallpaperName ->
                            goToDetail(wallpaperId, wallpaperName)
                        }
                    }
                }
            }
        }
    }
}
