package com.sefford.artdrian.wallpapers.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sefford.artdrian.common.stores.HoldsState
import com.sefford.artdrian.wallpapers.domain.model.Wallpaper
import com.sefford.artdrian.wallpapers.store.WallpaperStore
import com.sefford.artdrian.wallpapers.store.WallpapersState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class WallpaperListViewModel @AssistedInject constructor(
    private val wallpaperStore: WallpaperStore,
    @Assisted initial: ViewState
) : ViewModel(), HoldsState<WallpaperListViewModel.ViewState> {

    override val state: StateFlow<ViewState> = wallpaperStore.state.map { ViewState(it) }
        .stateIn(viewModelScope, SharingStarted.Lazily, initial)

    sealed class ViewState {
        data object Loading : ViewState()
        class Content(val wallpapers: List<Wallpaper>) : ViewState()

        class Error(val error: Errors) : ViewState()

        companion object {
            operator fun invoke(state: WallpapersState): ViewState = when (state) {
                is WallpapersState.Idle -> Loading
                is WallpapersState.Loaded -> Content(state.wallpapers.map { it })
                is WallpapersState.Error -> Error(Errors.NotFoundError(""))
            }
        }
    }

    sealed class Errors {
        class NetworkError(val status: Int) : Errors()
        class NotFoundError(val id: String) : Errors()
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted initialState: ViewState,
        ): WallpaperListViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {

        fun providesFactory(
            assistedFactory: Factory,
            initialState: ViewState
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(initialState) as T
            }
        }
    }
}
