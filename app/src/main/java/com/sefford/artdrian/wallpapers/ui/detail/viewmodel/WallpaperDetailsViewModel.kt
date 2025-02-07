package com.sefford.artdrian.wallpapers.ui.detail.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sefford.artdrian.common.data.DataError
import com.sefford.artdrian.common.di.Default
import com.sefford.artdrian.common.di.Id
import com.sefford.artdrian.common.stores.DispatchesEffects
import com.sefford.artdrian.common.stores.HoldsState
import com.sefford.artdrian.common.stores.StoreEffectDispatching
import com.sefford.artdrian.wallpapers.store.WallpaperStore
import com.sefford.artdrian.wallpapers.store.WallpapersState
import com.sefford.artdrian.wallpapers.ui.detail.effects.WallpaperDetailsEffect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class WallpaperDetailsViewModel(
    wallpapers: WallpaperStore,
    initial: WallpaperDetailsState,
    id: String,
    scope: CoroutineScope
) : ViewModel(), HoldsState<WallpaperDetailsState>, DispatchesEffects<WallpaperDetailsEffect> by StoreEffectDispatching(scope) {

    override val state: StateFlow<WallpaperDetailsState> =
        wallpapers.state
            .map { state ->
                when (state) {
                    is WallpapersState.Loaded -> state[id]
                        .fold({ WallpapersState.Error(DataError.Network.NotFound(id)) })
                        { wallpaper -> WallpapersState.Loaded(wallpaper) }
                    else -> state
                }
            }
            .map { state -> WallpaperDetailsState(state, ::effect) }
            .stateIn(viewModelScope, SharingStarted.Lazily, initial)

    override val current: WallpaperDetailsState
        get() = state.value

//    fun downloadWallpaper(): Flow<DownloadResult> {
//        return flow {
//            store.state.value[id].fold({
//                emit(DownloadResult.Error(IllegalStateException("Wallpaper not ready")))
//            }) { wallpaper ->
//                when (val response = downloadWallpaper.download(wallpaper.images.mobile)) {
//                    is Either.Left -> emit(DownloadResult.Error(response.value.exception))
//                    is Either.Right -> emit(DownloadResult.Response(response.value))
//                }
//            }
//        }.flowOn(dispatcher)
//    }

//    fun applyWallpaper(): Flow<SetWallpaperResult> {
//        return flow {
//            downloadWallpaper().collect { result ->
//                when (result) {
//                    is DownloadResult.Error -> SetWallpaperResult.Error(result.error)
//                    is DownloadResult.Response -> {
//                        when (val wallpaperResponse = setWallpaper.setWallpaper(result.uri)) {
//                            is Either.Left -> emit(SetWallpaperResult.Error(wallpaperResponse.value))
//                            is Either.Right -> emit(SetWallpaperResult.Ok)
//                        }
//                    }
//                }
//            }
//        }.flowOn(dispatcher)
//    }

    sealed class DownloadResult {
        class Error(val error: Throwable) : DownloadResult()
        class Response(val uri: String) : DownloadResult()
    }

    sealed class SetWallpaperResult {
        class Error(val error: Throwable) : SetWallpaperResult()
        object Ok : SetWallpaperResult()
    }

    class Provider @Inject constructor(
        private val wallpaperStore: WallpaperStore,
        @Id private val id: String,
        @Default private val scope: CoroutineScope
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WallpaperDetailsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return WallpaperDetailsViewModel(wallpaperStore, WallpaperDetailsState.Loading, id, scope) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
