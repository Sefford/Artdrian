package com.sefford.artdrian.wallpapers.ui.list.viewmodel

sealed class WallpaperListEffect {

    class GoToDetail(val id: String, val name: String): WallpaperListEffect(), Navigation

    sealed interface Navigation
}
