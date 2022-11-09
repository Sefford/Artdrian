package com.sefford.artdrian.wallpaperdetail.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.sefford.artdrian.R
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
                        WallpaperDetailScreen(
                            viewState = viewState,
                            name = name,
                            onSaveClicked = { saveWallpaper() },
                            onApplyClicked = { applyWallpaper() })
                    }
                }
            }
        }
    }

    private fun saveWallpaper() {
        lifecycleScope.launch {
            viewModel.downloadWallpaper().flowOn(Dispatchers.IO).collect { result ->
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
            viewModel.applyWallpaper().flowOn(Dispatchers.IO).collect { result ->
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
