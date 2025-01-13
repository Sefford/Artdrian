package com.sefford.artdrian.wallpapers.store

import com.sefford.artdrian.downloads.domain.model.Download
import com.sefford.artdrian.downloads.domain.model.Downloads
import com.sefford.artdrian.wallpapers.domain.model.Wallpaper

sealed class WallpaperEffects {

    data object LoadAll: WallpaperEffects()

    class Load(val id: String): WallpaperEffects()

    class Persist(val metadata: List<Wallpaper>): WallpaperEffects()

    class PrepareDownloads(val downloads: Downloads): WallpaperEffects()

    data object Clear: WallpaperEffects()
}
