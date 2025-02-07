package com.sefford.artdrian.common.ui.navigation

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sefford.artdrian.common.di.ApplicationComponent
import com.sefford.artdrian.common.di.ScreenModule
import com.sefford.artdrian.common.utils.disableFullscreen
import com.sefford.artdrian.common.utils.enableFullscreen
import com.sefford.artdrian.wallpapers.ui.detail.WallpaperDetailScreen
import com.sefford.artdrian.wallpapers.ui.detail.di.WallpaperDetailModule
import com.sefford.artdrian.wallpapers.ui.detail.effects.bridgeEffectHandler
import com.sefford.artdrian.wallpapers.ui.detail.viewmodel.WallpaperDetailsViewModel
import com.sefford.artdrian.wallpapers.ui.list.WallpaperListScreen
import com.sefford.artdrian.wallpapers.ui.list.effects.bridgeEffectHandler
import com.sefford.artdrian.wallpapers.ui.list.viewmodel.WallpaperListViewModel

@Composable
fun Artdrian(graph: ApplicationComponent) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable(Routes.WallpaperList.blueprint) { backstack ->
            val injection = graph.plus(ScreenModule(navController))
            val viewModel: WallpaperListViewModel = viewModel<WallpaperListViewModel>(
                viewModelStoreOwner = backstack,
                factory = injection.viewModel()
            ).also { vm ->
                vm.bridgeEffectHandler(injection.effectHandler()::handle, injection.mainScope())
            }
            (LocalActivity.current as ComponentActivity).window.disableFullscreen()
            WallpaperListScreen(viewModel.state.collectAsStateWithLifecycle().value)
        }
        composable(Routes.WallpaperDetail.blueprint) { backstack ->
            val injection = graph.plus(
                ScreenModule(navController),
                WallpaperDetailModule(backstack.arguments?.getString("id").orEmpty())
            )
            val viewModel: WallpaperDetailsViewModel = viewModel<WallpaperDetailsViewModel>(
                viewModelStoreOwner = backstack,
                factory = injection.viewModel()
            ).also { vm ->
                vm.bridgeEffectHandler(injection.effectHandler()::handle, injection.defaultScope())
            }
            (LocalActivity.current as ComponentActivity).window.enableFullscreen()
            WallpaperDetailScreen(state = viewModel.state.collectAsStateWithLifecycle().value)
        }
    }

}
