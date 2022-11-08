package com.sefford.artdrian.wallpaperdetail.ui

import androidx.lifecycle.ViewModel
import arrow.core.Either
import arrow.core.toOption
import com.sefford.artdrian.model.Wallpaper
import com.sefford.artdrian.usecases.GetWallpaper
import com.sefford.artdrian.wallpaperdetail.di.WallpaperId
import com.sefford.artdrian.wallpaperdetail.ui.WallpaperDetailViewModel.ViewState.Loading
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WallpaperDetailViewModel : ViewModel() {

    @Inject
    protected lateinit var getWallpaper: GetWallpaper
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
        }
    }

    sealed class ViewState {
        object Loading : ViewState()
        class Content(val wallpaper: Wallpaper) : ViewState()
        class NotFound(id: String) : ViewState()
    }
}
