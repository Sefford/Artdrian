package com.sefford.artdrian.wallpaperdetail.ui

import androidx.lifecycle.ViewModel
import arrow.core.Either
import arrow.core.toOption
import com.sefford.artdrian.model.Wallpaper
import com.sefford.artdrian.usecases.DownloadWallpaper
import com.sefford.artdrian.usecases.GetWallpaper
import com.sefford.artdrian.usecases.SetWallpaper
import com.sefford.artdrian.wallpaperdetail.di.WallpaperId
import com.sefford.artdrian.wallpaperdetail.ui.WallpaperDetailViewModel.ViewState.Loading
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class WallpaperDetailViewModel : ViewModel() {

    @Inject
    protected lateinit var getWallpaper: GetWallpaper

    @Inject
    protected lateinit var downloadWallpaper: DownloadWallpaper

    @Inject
    protected lateinit var setWallpaper: SetWallpaper

    @Inject
    protected lateinit var dispatcher: CoroutineDispatcher

    @Inject
    @WallpaperId
    protected lateinit var id: String

    private var wallpaper: Wallpaper? = null

    fun getWallpaper(): Flow<ViewState> {
        return flow {
            wallpaper.toOption()
                .fold({
                    emit(Loading)
                    when (val response = getWallpaper.getWallpaper(id)) {
                        is Either.Left -> ViewState.NotFound(id)
                        is Either.Right -> {
                            wallpaper = response.value
                            emit(ViewState.Content(response.value))
                        }
                    }
                }) { wallpaper -> emit(ViewState.Content(wallpaper)) }
        }.flowOn(dispatcher)
    }

    fun downloadWallpaper(): Flow<DownloadResult> {
        return flow {
            wallpaper.toOption().fold({
                emit(DownloadResult.Error(IllegalStateException("Wallpaper not ready")))
            }) { wallpaper ->
                when (val response = downloadWallpaper.download(wallpaper.mobile)) {
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
    object Loading : ViewState()
    class Content(val wallpaper: Wallpaper) : ViewState()
    class NotFound(id: String) : ViewState()
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
