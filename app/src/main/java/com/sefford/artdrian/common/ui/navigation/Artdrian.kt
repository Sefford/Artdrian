package com.sefford.artdrian.common.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sefford.artdrian.common.di.ApplicationComponent
import com.sefford.artdrian.wallpapers.ui.list.WallpaperListScreen
import com.sefford.artdrian.wallpapers.ui.list.WallpaperListViewModel
import com.sefford.artdrian.wallpapers.ui.list.di.WallpaperListModule

@Composable
fun Artdrian(graph: ApplicationComponent) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") { backstack ->
            val viewModel: WallpaperListViewModel = viewModel(
                viewModelStoreOwner = backstack,
                factory = graph.plus(WallpaperListModule(navController)).viewModel()
            )
            WallpaperListScreen(viewModel.state.collectAsStateWithLifecycle().value)
        }
//        composable("details/{id}") { WallpaperDetailScreen(navController) }
    }

}
