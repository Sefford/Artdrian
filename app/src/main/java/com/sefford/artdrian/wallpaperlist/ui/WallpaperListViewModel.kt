package com.sefford.artdrian.wallpaperlist.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import arrow.core.Either
import com.sefford.artdrian.model.Wallpaper
import com.sefford.artdrian.usecases.GetWallpapers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WallpaperListViewModel : ViewModel() {

    @Inject
    protected lateinit var getWallpapers: GetWallpapers

     private val wallpapers: MutableList<Wallpaper> = mutableListOf()

    fun getWallpapers(): Flow<ViewState> {
        return flow {
            if (wallpapers.isNotEmpty()) {
                emit(ViewState.Content(wallpapers))
            } else {
                emit(ViewState.Loading)
                when (val response = getWallpapers.getWallpapers()) {
                    is Either.Left -> emit(ViewState.NetworkError)
                    is Either.Right -> {
                        wallpapers.addAll(response.value)
                        emit(ViewState.Content(wallpapers))
                    }
                }
            }
        }
    }

    sealed class ViewState {
        object Loading : ViewState()
        class Content(val wallpapers: List<Wallpaper>): ViewState()
        object NetworkError : ViewState()
    }
}
