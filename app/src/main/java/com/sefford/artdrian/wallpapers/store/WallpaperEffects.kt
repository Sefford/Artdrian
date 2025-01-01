package com.sefford.artdrian.wallpapers.store

import com.sefford.artdrian.model.Wallpaper

sealed class WallpaperEffects {

    data object LoadAll: WallpaperEffects()

    class Load(val id: String): WallpaperEffects()

    class Persist(val metadata: List<Wallpaper>): WallpaperEffects()

    data object Clear: WallpaperEffects()
}
