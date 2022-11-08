package com.sefford.artdrian.wallpaperlist.ui

import androidx.lifecycle.ViewModel
import arrow.core.Either
import com.sefford.artdrian.datasources.WallpaperRepository
import com.sefford.artdrian.datasources.WallpaperRepository.*
import com.sefford.artdrian.datasources.WallpaperRepository.RepositoryError.NetworkingError
import com.sefford.artdrian.datasources.WallpaperRepository.RepositoryError.NotFound
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
                    is Either.Left -> when(val error = response.value){
                        is NetworkingError -> emit(ViewState.Error(Errors.NetworkError(error.status)))
                        is NotFound -> emit(ViewState.Error(Errors.NotFoundError(error.id)))
                    }
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
        class Error(val error: Errors) : ViewState()
    }

    sealed class Errors {
        class NetworkError(val status: Int): Errors()
        class NotFoundError(val id: String): Errors()
    }
}
