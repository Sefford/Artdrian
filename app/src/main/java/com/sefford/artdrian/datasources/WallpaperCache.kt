package com.sefford.artdrian.datasources

import com.sefford.artdrian.model.Wallpaper

interface WallpaperCache {
    suspend fun save(wallpapers: List<Wallpaper>)

    suspend fun save(wallpaper: Wallpaper)

    suspend fun delete(id: String)

    suspend fun clear()
}
