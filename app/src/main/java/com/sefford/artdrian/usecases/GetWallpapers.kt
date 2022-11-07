package com.sefford.artdrian.usecases

import arrow.core.Either
import com.sefford.artdrian.datasources.WallpaperRepository
import com.sefford.artdrian.datasources.WallpaperRepository.RepositoryError
import com.sefford.artdrian.model.Wallpaper
import javax.inject.Inject

class GetWallpapers @Inject constructor(private val repository: WallpaperRepository) {

    suspend fun getWallpapers(): Either<RepositoryError, List<Wallpaper>> {
        return repository.getAllMetadata().map { allMetadata ->
            allMetadata.map { metadata ->
                Wallpaper(metadata)
            }
        }
    }
}
