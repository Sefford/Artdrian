package com.sefford.artdrian.wallpapers.data.datasources

import com.sefford.artdrian.wallpapers.domain.model.Wallpaper

interface WallpaperCache {
    suspend fun save(wallpapers: List<Wallpaper>)

    suspend fun save(wallpaper: Wallpaper)

    suspend fun delete(id: String)

    suspend fun clear()
}
