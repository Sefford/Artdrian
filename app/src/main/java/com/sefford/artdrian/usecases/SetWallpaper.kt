package com.sefford.artdrian.usecases

import arrow.core.Either
import com.sefford.artdrian.common.WallpaperAdapter
import javax.inject.Inject

class SetWallpaper @Inject constructor(private val wallpaperManager: WallpaperAdapter) {

    suspend fun setWallpaper(wallpaper: String): Either<Throwable, Unit> =
        Either.catch { wallpaperManager.setWallpaper(wallpaper) }

}
