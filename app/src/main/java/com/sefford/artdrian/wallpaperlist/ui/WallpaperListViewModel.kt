package com.sefford.artdrian.wallpaperlist.ui

import androidx.lifecycle.ViewModel
import com.sefford.artdrian.model.Wallpaper
import com.sefford.artdrian.wallpapers.store.WallpaperStore
import com.sefford.artdrian.wallpapers.store.WallpapersState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WallpaperListViewModel : ViewModel() {

    @Inject
    protected lateinit var store: WallpaperStore

    val wallpapers: Flow<ViewState>
        get() = store.state.map { ViewState(it) }

    sealed class ViewState {
        data object Loading : ViewState()
        class Content(val wallpapers: List<Wallpaper>): ViewState()
        class Error(val error: Errors) : ViewState()

        companion object {
            operator fun invoke(state: WallpapersState): ViewState = when(state) {
                WallpapersState.Idle -> Loading
                is WallpapersState.Loaded -> Content(state.wallpapers.map { it })
                is WallpapersState.Error -> Error(Errors.NotFoundError(""))
            }
        }
    }

    sealed class Errors {
        class NetworkError(val status: Int): Errors()
        class NotFoundError(val id: String): Errors()
    }
}
