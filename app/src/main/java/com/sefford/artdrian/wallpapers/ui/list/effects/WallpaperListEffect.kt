package com.sefford.artdrian.wallpapers.ui.list.effects

sealed class WallpaperListEffect {

    class GoToDetail(val id: String, val name: String): WallpaperListEffect(), Navigation

    sealed interface Navigation
}
