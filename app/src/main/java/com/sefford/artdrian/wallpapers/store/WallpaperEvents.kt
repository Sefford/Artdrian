package com.sefford.artdrian.wallpapers.store

import com.sefford.artdrian.data.DataError
import com.sefford.artdrian.model.Wallpaper
import com.sefford.artdrian.model.WallpaperList

sealed class WallpaperEvents {

    data object Load: WallpaperEvents()

    data object Refresh: WallpaperEvents()

    class OnResponseReceived(val response: WallpaperList): WallpaperEvents() {
        constructor(wallpaper: Wallpaper): this(wallpaper.toList())
    }

    class OnErrorReceived(val error: DataError) : WallpaperEvents()

}
