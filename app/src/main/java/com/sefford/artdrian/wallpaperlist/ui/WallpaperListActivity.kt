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
import com.sefford.artdrian.wallpapers.store.WallpaperStore
import javax.inject.Inject

class WallpaperListActivity : ComponentActivity() {

    @Inject
    protected lateinit var store: WallpaperStore

    private val viewModel: WallpaperListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        graph.inject(viewModel)
        setContent {
            WallpaperListScreen(
                viewModel.wallpapers.collectAsState(
                    WallpaperListViewModel.ViewState.Loading,
                    lifecycleScope.coroutineContext
                ).value
            )
            { wallpaperId, wallpaperName -> goToDetail(wallpaperId, wallpaperName) }
        }
    }
}
