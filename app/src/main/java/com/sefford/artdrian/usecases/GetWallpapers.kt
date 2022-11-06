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
                Wallpaper(
                    metadata = metadata,
                    desktop = BASE_URL.format(Type.DESKTOP.identifier, metadata.slug),
                    mobile = BASE_URL.format(Type.MOBILE.identifier, metadata.slug),
                )
            }
        }
    }

    companion object {
        val BASE_URL = "https://adrianmato.art/static/downloads/%s/%s.png"
    }

    enum class Type(val identifier: String) {
        DESKTOP("desktop"),
        MOBILE("mobile")
    }
}
