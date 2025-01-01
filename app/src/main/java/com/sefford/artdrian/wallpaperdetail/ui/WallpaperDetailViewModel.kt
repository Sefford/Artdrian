package com.sefford.artdrian.wallpaperdetail.ui

import androidx.lifecycle.ViewModel
import arrow.core.Either
import arrow.core.Option
import arrow.core.toOption
import com.sefford.artdrian.model.Metadata
import com.sefford.artdrian.model.Wallpaper
import com.sefford.artdrian.usecases.DownloadWallpaper
import com.sefford.artdrian.usecases.SetWallpaper
import com.sefford.artdrian.wallpaperdetail.di.WallpaperId
import com.sefford.artdrian.wallpapers.store.WallpaperStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WallpaperDetailViewModel : ViewModel() {

    @Inject
    protected lateinit var store: WallpaperStore

    @Inject
    protected lateinit var downloadWallpaper: DownloadWallpaper

    @Inject
    protected lateinit var setWallpaper: SetWallpaper

    @Inject
    protected lateinit var dispatcher: CoroutineDispatcher

    @Inject
    @WallpaperId
    protected lateinit var id: String

    val wallpaper: Flow<ViewState>
        get() = store.state.map {
            ViewState(it[id])
        }

    fun downloadWallpaper(): Flow<DownloadResult> {
        return flow {
            store.state.value[id].fold({
                emit(DownloadResult.Error(IllegalStateException("Wallpaper not ready")))
            }) { wallpaper ->
                when (val response = downloadWallpaper.download(wallpaper.metadata.mobile)) {
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
    class Content(val wallpaper: Metadata) : ViewState()
    class NotFound(id: String) : ViewState()

    companion object {
        operator fun invoke(wallpaper: Option<Wallpaper>) = wallpaper.fold({
            NotFound("")
        }) {
            Content(it.metadata)
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
