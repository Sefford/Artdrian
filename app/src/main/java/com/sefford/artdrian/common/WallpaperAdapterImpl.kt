package com.sefford.artdrian.common

import android.app.WallpaperManager
import android.graphics.BitmapFactory
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WallpaperAdapterImpl @Inject constructor(private val wallpaperManager: WallpaperManager) : WallpaperAdapter {

    override suspend fun setWallpaper(wallpaper: String) {
        wallpaperManager.setBitmap(BitmapFactory.decodeStream(URL(wallpaper).openStream()))
    }
}
