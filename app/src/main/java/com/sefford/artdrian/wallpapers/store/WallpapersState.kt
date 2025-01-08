package com.sefford.artdrian.wallpapers.store

import arrow.core.None
import arrow.core.Option
import arrow.core.toOption
import com.sefford.artdrian.common.data.DataError
import com.sefford.artdrian.wallpapers.domain.model.Sourced
import com.sefford.artdrian.wallpapers.domain.model.Wallpaper
import com.sefford.artdrian.wallpapers.domain.model.WallpaperList

sealed class WallpapersState : Sourced {

    sealed class Idle : WallpapersState(), Sourced by Sourced.Local {
        override fun get(id: String): Option<Wallpaper> = None

        override fun plus(list: WallpaperList): WallpapersState =
            Loaded(list)

        override fun plus(wallpaper: Wallpaper): WallpapersState =
            Loaded(wallpaper)

        data object Empty : Idle() {
            override fun plus(error: DataError): WallpapersState = if (error.network) OnNetworkError(error) else OnLocalError(error)
        }

        // This serves to hold the error until we wait for the other half to arrive
        class OnNetworkError(val error: DataError) : Idle() {
            override fun plus(error: DataError): WallpapersState = if (error.local) Error(this.error) else OnNetworkError(error)
        }

        class OnLocalError(val error: DataError) : Idle() {
            override fun plus(error: DataError): WallpapersState = if (error.network) Error(error) else OnLocalError(error)
        }
    }

    class Loaded(
        val wallpapers: List<Wallpaper>,
        private val sourced: Sourced,
        val next: WallpapersState = Idle.Empty,
    ) : WallpapersState(), Sourced by sourced {

        constructor(list: WallpaperList) : this(list.wallpapers, list.source)

        constructor(wallpaper: Wallpaper) : this(wallpaper.toList())

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
            return Loaded(merged.values.toList(), if (source.network) source else list.source, Idle.Empty)
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

    abstract operator fun plus(list: WallpaperList): WallpapersState

    abstract operator fun plus(wallpaper: Wallpaper): WallpapersState

    abstract operator fun plus(error: DataError): WallpapersState

    abstract operator fun get(id: String): Option<Wallpaper>

}
