package com.sefford.artdrian.wallpapers.domain.model

sealed class WallpaperList(val wallpapers: List<Wallpaper>) : Sourced {

    class FromLocal(wallpapers: List<Wallpaper>) : WallpaperList(wallpapers), Sourced by Sourced.Local {
        constructor(wallpaper: Wallpaper) : this(listOf(wallpaper))

        override val transient: List<Wallpaper> = emptyList()

        override fun toLocal(): WallpaperList = this

        override fun toNetwork(): WallpaperList = FromNetwork(wallpapers)

    }

    class FromNetwork(wallpapers: List<Wallpaper>) : WallpaperList(wallpapers), Sourced by Sourced.Network {
        constructor(wallpaper: Wallpaper) : this(listOf(wallpaper))

        override val transient: List<Wallpaper> = wallpapers

        override fun toLocal(): WallpaperList = FromLocal(wallpapers)

        override fun toNetwork(): WallpaperList = this

    }

    abstract val transient: List<Wallpaper>

    abstract fun toLocal(): WallpaperList

    abstract fun toNetwork(): WallpaperList

    override fun toString(): String {
        return "WallpaperList(wallpapers=${wallpapers.size}, ${source.source})"
    }

    companion object {
        operator fun invoke(wallpaper: Wallpaper) = wallpaper.toList()
    }

}
