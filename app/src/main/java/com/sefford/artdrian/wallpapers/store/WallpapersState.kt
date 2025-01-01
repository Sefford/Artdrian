package com.sefford.artdrian.wallpapers.store

import arrow.core.None
import arrow.core.Option
import arrow.core.toOption
import com.sefford.artdrian.data.DataError
import com.sefford.artdrian.model.Metadata
import com.sefford.artdrian.model.Source
import com.sefford.artdrian.model.Wallpaper
import com.sefford.artdrian.model.WallpaperList

sealed class WallpapersState {

    data object Idle : WallpapersState() {
        override val transient: List<Metadata> = emptyList()

        override fun plus(response: WallpaperList): WallpapersState =
            Loaded(response.wallpapers.map { Wallpaper(it, response.source) }, source = response.source, Idle)

        override fun plus(response: Wallpaper): WallpapersState =
            Loaded(listOf(response), source = response.source, Idle)

        override fun plus(error: DataError): WallpapersState = if (error.source.network) Error(error) else this

        override fun get(id: String): Option<Wallpaper> = None

    }

    class Loaded(
        val wallpapers: List<Wallpaper>,
        val source: Source,
        val next: WallpapersState
    ) : WallpapersState() {

        override val transient: List<Metadata> by lazy {
            if (source.local) {
                emptyList()
            } else {
                wallpapers.filter { wallpaper -> wallpaper.source.network }
                    .map { wallpaper -> wallpaper.metadata }
            }
        }

        override fun plus(response: WallpaperList): WallpapersState {
            val merged = LinkedHashMap<String, Wallpaper>()
            wallpapers.forEach { wallpaper -> merged[wallpaper.metadata.id] = wallpaper }
            response.wallpapers.forEach { wallpaper ->
                wallpaper.takeIf {
                    val inListSource = merged[wallpaper.id]?.source
                    inListSource == null || response.source.network || inListSource == response.source
                }?.let {
                    merged[wallpaper.id] = Wallpaper(wallpaper, response.source)
                }
            }
            return Loaded(merged.values.toList(), if (source.network) source else response.source, Idle)
        }

        override fun plus(response: Wallpaper): WallpapersState =
            this + WallpaperList(response.metadata, response.source)

        override fun plus(error: DataError): WallpapersState = if (
            error.source == (next as? Error)?.source || error.source.network
        ) {
            Loaded(wallpapers, error.source, Error(error))
        } else {
            this
        }

        override fun get(id: String): Option<Wallpaper> = wallpapers.find { it.metadata.id == id }.toOption()

        override fun toString(): String {
            return "Loaded(wallpapers=${wallpapers.size}, source=$source, next=$next)"
        }
    }

    class Error(val error: DataError) : WallpapersState() {

        val source: Source = error.source
        override val transient: List<Metadata> = emptyList()

        // Should be set the next to Error?
        override fun plus(response: WallpaperList): WallpapersState =
            Loaded(response.wallpapers.map { Wallpaper(it, response.source) }, source = response.source, Idle)

        override fun plus(response: Wallpaper): WallpapersState =
            Loaded(listOf(response), source = response.source, Idle)

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

    abstract val transient: List<Metadata>

    abstract operator fun plus(response: WallpaperList): WallpapersState

    abstract operator fun plus(response: Wallpaper): WallpapersState

    abstract operator fun plus(error: DataError): WallpapersState

    abstract operator fun get(id: String): Option<Wallpaper>

}
