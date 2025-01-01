package com.sefford.artdrian.model

import com.sefford.artdrian.data.dto.WallpaperDatabaseDto
import com.sefford.artdrian.data.dto.WallpaperNetworkDto
import kotlinx.datetime.LocalDateTime
import java.util.Locale

sealed class Wallpaper(
    val id: String,
    val title: String,
    val views: Int,
    val downloads: Int,
    val slug: String,
    val created: LocalDateTime,
    val updated: LocalDateTime = created,
) : Sourced {

    class FromLocal(
        id: String,
        title: String,
        views: Int,
        downloads: Int,
        slug: String,
        created: LocalDateTime,
        updated: LocalDateTime = created
    ) : Wallpaper(id, title, views, downloads, slug, created, updated), Sourced by Sourced.Local {

        override fun toList(): WallpaperList = WallpaperList.FromLocal(this)

        override fun toNetwork(): Wallpaper = FromNetwork(id, title, views, downloads, slug, created, updated)

        override fun toLocal(): Wallpaper = this

    }

    class FromNetwork(
        id: String,
        title: String,
        views: Int,
        downloads: Int,
        slug: String,
        created: LocalDateTime,
        updated: LocalDateTime = created
    ) : Wallpaper(id, title, views, downloads, slug, created, updated), Sourced by Sourced.Network {

        override fun toList(): WallpaperList = WallpaperList.FromNetwork(this)

        override fun toNetwork(): Wallpaper = this

        override fun toLocal(): Wallpaper = FromLocal(id, title, views, downloads, slug, created, updated)
    }

    val desktop: String by lazy {
        BASE_URL.format(Type.DESKTOP.identifier, slug, (if (isPng) Extension.PNG else Extension.JPG).extension)
    }
    val mobile: String by lazy {
        BASE_URL.format(Type.MOBILE.identifier, slug, (if (isPng) Extension.PNG else Extension.JPG).extension)
    }
    val name: String by lazy {
        slug.split("_")
            .joinToString(separator = " ") { word ->
                if (word.toIntOrNull() == null) word.replaceFirstChar { it.uppercase(Locale.ENGLISH) } else "#${word}"
            }
    }

    val isPng: Boolean by lazy { this.slug.contains("png") }

    abstract fun toList(): WallpaperList

    abstract fun toNetwork(): Wallpaper

    abstract fun toLocal(): Wallpaper

    private enum class Type(val identifier: String) {
        DESKTOP("desktop"),
        MOBILE("mobile")
    }

    private enum class Extension(val extension: String) {
        PNG("png"),
        JPG("jpg")
    }

    companion object {
        private val BASE_URL = "https://adrianmato.art/static/downloads/%s/%s.%s"

        operator fun invoke(dto: WallpaperNetworkDto) = FromNetwork(
            id = dto.id,
            title = dto.title,
            views = 0,
            downloads = dto.downloads,
            slug = dto.slug,
            created = dto.created
        )

        operator fun invoke(dto: WallpaperDatabaseDto) = FromLocal(
            id = dto.id,
            title = dto.title,
            views = dto.views,
            downloads = dto.downloads,
            slug = dto.slug,
            created = LocalDateTime.parse(dto.created),
            updated = LocalDateTime.parse(dto.updated)
        )
    }

}
