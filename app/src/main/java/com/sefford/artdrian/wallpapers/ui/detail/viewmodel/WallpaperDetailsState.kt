package com.sefford.artdrian.wallpapers.ui.detail.viewmodel

import com.sefford.artdrian.common.data.DataError
import com.sefford.artdrian.wallpapers.store.WallpapersState
import com.sefford.artdrian.wallpapers.ui.detail.Listeners
import com.sefford.artdrian.wallpapers.ui.detail.WallpaperDetail
import com.sefford.artdrian.wallpapers.ui.detail.effects.WallpaperDetailsEffect

sealed class WallpaperDetailsState {
    data object Loading : WallpaperDetailsState()
    class Content(val wallpaper: WallpaperDetail) : WallpaperDetailsState()
    sealed class Error() : WallpaperDetailsState() {

        data object NotFound : Error()

        data object Network : Error()

        data object Critical : Error()

        data object Empty : Error()

        companion object {
            operator fun invoke(error: DataError) = when (error) {
                DataError.Local.Empty, is DataError.Local.NotFound -> Empty
                DataError.Network.ConnectTimeout,
                DataError.Network.NoConnection,
                DataError.Network.SocketTimeout,
                is DataError.Network.NotFound,
                is DataError.Network.Invalid -> Network
                is DataError.Network.Critical, is DataError.Local.Critical -> Critical
            }
        }
    }

    companion object {
        operator fun invoke(state: WallpapersState, effects: (WallpaperDetailsEffect) -> Unit): WallpaperDetailsState =
            when (state) {
                is WallpapersState.Idle -> Loading
                is WallpapersState.Loaded -> Content(
                    WallpaperDetail(state.wallpapers.first(), Listeners(state.wallpapers.first(), effects))
                )
                is WallpapersState.Error -> Error(state.error)
            }
    }
}
