package com.sefford.artdrian.wallpaperlist.ui

import androidx.lifecycle.ViewModel
import arrow.core.Either
import com.sefford.artdrian.model.Metadata
import com.sefford.artdrian.stores.Store
import com.sefford.artdrian.wallpapers.store.WallpaperEffects
import com.sefford.artdrian.wallpapers.store.WallpaperEvents
import com.sefford.artdrian.wallpapers.store.WallpaperStore
import com.sefford.artdrian.wallpapers.store.WallpapersState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WallpaperListViewModel : ViewModel() {

    @Inject
    protected lateinit var store: WallpaperStore

    val wallpapers: Flow<ViewState>
        get() = store.state.map { ViewState(it) }

    sealed class ViewState {
        data object Loading : ViewState()
        class Content(val wallpapers: List<Metadata>): ViewState()
        class Error(val error: Errors) : ViewState()

        companion object {
            operator fun invoke(state: WallpapersState): ViewState = when(state) {
                WallpapersState.Idle -> Loading
                is WallpapersState.Loaded -> Content(state.wallpapers.map { it.metadata })
                is WallpapersState.Error -> Error(Errors.NotFoundError(""))
            }
        }
    }

    sealed class Errors {
        class NetworkError(val status: Int): Errors()
        class NotFoundError(val id: String): Errors()
    }
}
