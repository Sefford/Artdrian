package com.sefford.artdrian.wallpapers.ui.list.viewmodel

import com.sefford.artdrian.common.data.DataError
import com.sefford.artdrian.wallpapers.store.WallpapersState
import com.sefford.artdrian.wallpapers.ui.views.WallpaperCardState

sealed class WallpaperListState(val wallpapers: List<WallpaperCardState>) {

    data object Loading : WallpaperListState(List(5) { WallpaperCardState.Loading })

    class Content(wallpapers: List<WallpaperCardState.Content>) : WallpaperListState(wallpapers)

    sealed class Errors() : WallpaperListState(emptyList()) {
        data object Network : Errors()

        data object Critical : Errors()

        data object Empty : Errors()

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
        operator fun invoke(state: WallpapersState, effect: (WallpaperListEffect) -> Unit): WallpaperListState = when (state) {
            is WallpapersState.Idle -> Loading
            is WallpapersState.Loaded -> Content(state.wallpapers.map { WallpaperCardState(it, effect) })
            is WallpapersState.Error -> Errors(state.error)
        }
    }
}

sealed class Errors {
    class NetworkError(val status: Int) : Errors()
    class NotFoundError(val id: String) : Errors()
}
