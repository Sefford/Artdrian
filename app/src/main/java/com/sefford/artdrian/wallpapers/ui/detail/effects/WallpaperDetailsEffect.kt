package com.sefford.artdrian.wallpapers.ui.detail.effects

sealed class WallpaperDetailsEffect {

    class Apply(val id: String): WallpaperDetailsEffect()

    class Download(val id: String): WallpaperDetailsEffect()

    class Share(val id: String): WallpaperDetailsEffect()
}
