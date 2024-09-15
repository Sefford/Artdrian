package com.sefford.artdrian.usecases

import arrow.core.Either
import com.sefford.artdrian.data.datasources.WallpaperRepository
import com.sefford.artdrian.data.datasources.WallpaperRepository.RepositoryError
import com.sefford.artdrian.model.Wallpaper
import javax.inject.Inject

class GetWallpaper @Inject constructor(private val repository: WallpaperRepository) {

    suspend fun getWallpaper(id: String): Either<RepositoryError, Wallpaper> {
        return repository.getWallpaperMetadata(id).map { metadata -> Wallpaper(metadata) }
    }
}
