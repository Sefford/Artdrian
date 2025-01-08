package com.sefford.artdrian.wallpapers.ui.detail

import androidx.lifecycle.ViewModel
import arrow.core.Either
import arrow.core.Option
import com.sefford.artdrian.wallpapers.domain.model.Wallpaper
import com.sefford.artdrian.wallpapers.domain.usecases.DownloadWallpaper
import com.sefford.artdrian.wallpapers.domain.usecases.SetWallpaper
import com.sefford.artdrian.wallpapers.ui.detail.di.WallpaperId
import com.sefford.artdrian.wallpapers.store.WallpaperStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WallpaperDetailViewModel : ViewModel() {

    @Inject
    internal lateinit var store: WallpaperStore

    @Inject
    internal lateinit var downloadWallpaper: DownloadWallpaper

    @Inject
    internal lateinit var setWallpaper: SetWallpaper

    @Inject
    internal lateinit var dispatcher: CoroutineDispatcher

    @Inject
    @WallpaperId
    internal lateinit var id: String

    val wallpaper: Flow<ViewState>
        get() = store.state.map {
            ViewState(it[id])
        }

    fun downloadWallpaper(): Flow<DownloadResult> {
        return flow {
            store.state.value[id].fold({
                emit(DownloadResult.Error(IllegalStateException("Wallpaper not ready")))
            }) { wallpaper ->
                when (val response = downloadWallpaper.download(wallpaper.images.mobile)) {
                    is Either.Left -> emit(DownloadResult.Error(response.value.exception))
                    is Either.Right -> emit(DownloadResult.Response(response.value))
                }
            }
        }.flowOn(dispatcher)
    }

    fun applyWallpaper(): Flow<SetWallpaperResult> {
        return flow {
            downloadWallpaper().collect { result ->
                when(result) {
                    is DownloadResult.Error -> SetWallpaperResult.Error(result.error)
                    is DownloadResult.Response -> {
                        when (val wallpaperResponse = setWallpaper.setWallpaper(result.uri)) {
                            is Either.Left -> emit(SetWallpaperResult.Error(wallpaperResponse.value))
                            is Either.Right -> emit(SetWallpaperResult.Ok)
                        }
                    }
                }
        }
    }.flowOn(dispatcher)
}

sealed class ViewState {
    data object Loading : ViewState()
    class Content(val wallpaper: Wallpaper) : ViewState()
    class NotFound(id: String) : ViewState()

    companion object {
        operator fun invoke(wallpaper: Option<Wallpaper>) = wallpaper.fold({
            NotFound("")
        }) {
            Content(it)
        }
    }
}

sealed class DownloadResult {
    class Error(val error: Throwable) : DownloadResult()
    class Response(val uri: String) : DownloadResult()
}

sealed class SetWallpaperResult {
    class Error(val error: Throwable) : SetWallpaperResult()
    object Ok : SetWallpaperResult()
}
}
