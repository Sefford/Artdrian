package com.sefford.artdrian.common.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sefford.artdrian.common.di.ApplicationComponent
import com.sefford.artdrian.wallpapers.ui.detail.WallpaperDetailScreen
import com.sefford.artdrian.wallpapers.ui.detail.WallpaperDetailViewModel
import com.sefford.artdrian.wallpapers.ui.list.WallpaperListScreen
import com.sefford.artdrian.wallpapers.ui.list.WallpaperListViewModel
import com.sefford.artdrian.wallpapers.ui.list.di.WallpaperListModule
import com.sefford.artdrian.wallpapers.ui.list.effects.bridgeEffectHandler

@Composable
fun Artdrian(graph: ApplicationComponent) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable(Routes.WallpaperList.blueprint) { backstack ->
            val injection = graph.plus(WallpaperListModule(navController))
            val viewModel: WallpaperListViewModel = viewModel<WallpaperListViewModel>(
                viewModelStoreOwner = backstack,
                factory = injection.viewModel()
            ).also { vm -> vm.bridgeEffectHandler(injection.effectHandler()::handle, injection.mainScope())

            }
            WallpaperListScreen(viewModel.state.collectAsStateWithLifecycle().value)
        }
        composable(Routes.WallpaperDetail.blueprint) {
            WallpaperDetailScreen(WallpaperDetailViewModel.ViewState.Loading, "")
        }
    }

}
