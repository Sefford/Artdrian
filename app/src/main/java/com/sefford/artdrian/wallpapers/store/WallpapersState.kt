package com.sefford.artdrian.wallpapers.store

import arrow.core.None
import arrow.core.Option
import arrow.core.toOption
import com.sefford.artdrian.data.DataError
import com.sefford.artdrian.model.Sourced
import com.sefford.artdrian.model.Wallpaper
import com.sefford.artdrian.model.WallpaperList

sealed class WallpapersState : Sourced {

    data object Idle : WallpapersState(), Sourced by Sourced.Local {
        override val transient: List<Wallpaper> = emptyList()

        override fun plus(list: WallpaperList): WallpapersState =
            Loaded(list)

        override fun plus(wallpaper: Wallpaper): WallpapersState =
            Loaded(wallpaper)

        override fun plus(error: DataError): WallpapersState = if (error.network) Error(error) else this

        override fun get(id: String): Option<Wallpaper> = None

    }

    class Loaded(
        val wallpapers: List<Wallpaper>,
        private val sourced: Sourced,
        val next: WallpapersState = Idle,
    ) : WallpapersState(), Sourced by sourced {

        constructor(list: WallpaperList) : this(list.wallpapers, list.source)

        constructor(wallpaper: Wallpaper) : this(wallpaper.toList())

        override val transient: List<Wallpaper> by lazy {
            if (local) {
                emptyList()
            } else {
                wallpapers.filter { wallpaper -> wallpaper.network }
            }
        }

        override fun plus(list: WallpaperList): WallpapersState {
            val merged = LinkedHashMap<String, Wallpaper>()
            wallpapers.forEach { wallpaper -> merged[wallpaper.id] = wallpaper }
            list.wallpapers.forEach { wallpaper ->
                wallpaper.takeIf {
                    val inListSource = merged[wallpaper.id]?.source
                    inListSource == null || list.source.network || inListSource == list.source
                }?.let {
                    merged[wallpaper.id] = wallpaper
                }
            }
            return Loaded(merged.values.toList(), if (source.network) source else list.source, Idle)
        }

        override fun plus(wallpaper: Wallpaper): WallpapersState =
            this + wallpaper.toList()

        override fun plus(error: DataError): WallpapersState = if (
            error.source == (next as? Error)?.source || error.source.network
        ) {
            Loaded(wallpapers, error.source, Error(error))
        } else {
            this
        }

        override fun get(id: String): Option<Wallpaper> = wallpapers.find { it.id == id }.toOption()

        override fun toString(): String {
            return "Loaded(wallpapers=${wallpapers.size}, source=$source, next=$next)"
        }
    }

    class Error(val error: DataError) : WallpapersState(), Sourced by error.source {

        override val transient: List<Wallpaper> = emptyList()

        // Should be set the next to Error?
        override fun plus(list: WallpaperList): WallpapersState =
            Loaded(list)

        override fun plus(wallpaper: Wallpaper): WallpapersState =
            Loaded(wallpaper)

        override fun plus(error: DataError): WallpapersState = if (error.source == source || error.source.network) {
            Error(error)
        } else {
            this
        }

        override fun get(id: String): Option<Wallpaper> = None

        override fun toString(): String {
            return "Error(error=$error, source=$source)"
        }
    }

    abstract val transient: List<Wallpaper>

    abstract operator fun plus(list: WallpaperList): WallpapersState

    abstract operator fun plus(wallpaper: Wallpaper): WallpapersState

    abstract operator fun plus(error: DataError): WallpapersState

    abstract operator fun get(id: String): Option<Wallpaper>

}
