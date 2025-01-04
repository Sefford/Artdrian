package com.sefford.artdrian.wallpaperlist.ui

import WallpaperListScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.lifecycleScope
import com.sefford.artdrian.ui.navigation.goToDetail
import com.sefford.artdrian.utils.graph
import javax.inject.Inject

class WallpaperListActivity : ComponentActivity() {

    @Inject
    internal lateinit var viewModelFactory: WallpaperListViewModel.Factory

    private val viewModel: WallpaperListViewModel by viewModels {
        WallpaperListViewModel.providesFactory(viewModelFactory, WallpaperListViewModel.ViewState.Loading)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        graph.inject(this)
        setContent {
            WallpaperListScreen(
                viewModel.state.collectAsState(
                    WallpaperListViewModel.ViewState.Loading,
                    lifecycleScope.coroutineContext
                ).value
            )
            { wallpaperId, wallpaperName -> goToDetail(wallpaperId, wallpaperName) }
        }
    }
}
