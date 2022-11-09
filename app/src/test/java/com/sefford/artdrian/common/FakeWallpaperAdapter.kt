package com.sefford.artdrian.common

class FakeWallpaperAdapter(private val onResponse: () -> Unit = {}) : WallpaperAdapter {
    override suspend fun setWallpaper(wallpaper: String) {
        onResponse()
    }
}
