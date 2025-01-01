package com.sefford.artdrian.model

sealed class WallpaperList(val wallpapers: List<Wallpaper>) : Sourced {

    class FromLocal(wallpapers: List<Wallpaper>) : WallpaperList(wallpapers), Sourced by Sourced.Local {
        constructor(wallpaper: Wallpaper) : this(listOf(wallpaper))

        override fun toLocal(): WallpaperList = this

        override fun toNetwork(): WallpaperList = FromNetwork(wallpapers)
    }

    class FromNetwork(wallpapers: List<Wallpaper>) : WallpaperList(wallpapers), Sourced by Sourced.Network {
        constructor(wallpaper: Wallpaper) : this(listOf(wallpaper))

        override fun toLocal(): WallpaperList = FromLocal(wallpapers)

        override fun toNetwork(): WallpaperList = this
    }

    abstract fun toLocal(): WallpaperList

    abstract fun toNetwork(): WallpaperList

    companion object {
        operator fun invoke(wallpaper: Wallpaper) = wallpaper.toList()
    }

}
