package com.sefford.artdrian.wallpaperdetail.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.sefford.artdrian.utils.graph
import com.sefford.artdrian.wallpaperdetail.di.WallpaperDetailModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class WallpaperDetailActivity : ComponentActivity() {

    private val viewModel: WallpaperDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        graph.plus(WallpaperDetailModule(intent.getStringExtra(EXTRA_ID)!!)).inject(viewModel)
        requestWallpaper(intent.getStringExtra(EXTRA_NAME)!!)
    }

    private fun requestWallpaper(name: String) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getWallpaper().flowOn(Dispatchers.IO).collect { viewState ->
                    setContent {
                        WallpaperDetailScreen(viewState, name)
                    }
                }
            }
        }
    }

    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_NAME = "extra_name"
    }
}
