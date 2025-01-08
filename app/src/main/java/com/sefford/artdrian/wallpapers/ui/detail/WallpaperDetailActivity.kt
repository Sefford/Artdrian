package com.sefford.artdrian.wallpapers.ui.detail

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.lifecycleScope
import com.sefford.artdrian.R
import com.sefford.artdrian.common.utils.graph
import com.sefford.artdrian.wallpapers.ui.detail.di.WallpaperDetailModule
import kotlinx.coroutines.launch

class WallpaperDetailActivity : ComponentActivity() {
    private val viewModel: WallpaperDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        graph.plus(WallpaperDetailModule(intent.getStringExtra(EXTRA_ID)!!)).inject(viewModel)
        setContent {
            val state = viewModel.wallpaper.collectAsState(WallpaperDetailViewModel.ViewState.Loading, lifecycleScope.coroutineContext)
            WallpaperDetailScreen(
                viewState = state.value,
                name = "",
                onSaveClicked = { saveWallpaper() },
                onApplyClicked = { applyWallpaper() })
        }
    }

    private fun saveWallpaper() {
        lifecycleScope.launch {
            viewModel.downloadWallpaper().collect { result ->
                val message = when (result) {
                    is WallpaperDetailViewModel.DownloadResult.Error -> R.string.detail_save_saving_error
                    is WallpaperDetailViewModel.DownloadResult.Response -> R.string.detail_save_saving_success
                }
                Toast.makeText(this@WallpaperDetailActivity, message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun applyWallpaper() {
        lifecycleScope.launch {
            viewModel.applyWallpaper().collect { result ->
                val message = when (result) {
                    is WallpaperDetailViewModel.SetWallpaperResult.Error -> R.string.detail_apply_error
                    WallpaperDetailViewModel.SetWallpaperResult.Ok -> R.string.detail_apply_success
                }
                Toast.makeText(this@WallpaperDetailActivity, message, Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_NAME = "extra_name"
    }
}
