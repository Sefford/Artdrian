package com.sefford.artdrian.model

import com.sefford.artdrian.data.dto.WallpaperDatabaseDto
import com.sefford.artdrian.data.dto.WallpaperNetworkDto
import kotlinx.datetime.LocalDateTime

sealed class Wallpaper(
    val id: String,
    val slug: String,
    val version: String,
    val title: String,
    val downloads: Int,
    val images: Images,
    val tags: List<String>,
    val published: LocalDateTime,
) : Sourced {

    class FromLocal(
        id: String,
        slug: String,
        version: String,
        title: String,
        downloads: Int,
        images: Images,
        tags: List<String>,
        published: LocalDateTime,
    ) : Wallpaper(id, slug, version, title, downloads, images, tags, published), Sourced by Sourced.Local {

        override fun toList(): WallpaperList = WallpaperList.FromLocal(this)

        override fun toNetwork(): Wallpaper = FromNetwork(id, slug, version, title, downloads, images, tags, published)

        override fun toLocal(): Wallpaper = this

    }

    class FromNetwork(
        id: String,
        slug: String,
        version: String,
        title: String,
        downloads: Int,
        images: Images,
        tags: List<String>,
        published: LocalDateTime,
    ) : Wallpaper(id, slug, version, title, downloads, images, tags, published), Sourced by Sourced.Network {

        override fun toList(): WallpaperList = WallpaperList.FromNetwork(this)

        override fun toNetwork(): Wallpaper = this

        override fun toLocal(): Wallpaper = FromLocal(id, slug, version, title, downloads, images, tags, published)
    }

    abstract fun toList(): WallpaperList

    abstract fun toNetwork(): Wallpaper

    abstract fun toLocal(): Wallpaper

    companion object {

        operator fun invoke(dto: WallpaperNetworkDto) = FromNetwork(
            id = dto.id,
            slug = dto.slug,
            version = dto.version,
            title = "${dto.title} #${dto.version}",
            downloads = dto.downloads,
            images = Images(dto),
            tags = dto.tags.map { it.uppercase() },
            published = dto.published
        )

        operator fun invoke(dto: WallpaperDatabaseDto) = FromLocal(
            id = dto.id,
            slug = dto.slug,
            version = dto.version,
            title = "${dto.title} #${dto.version}",
            downloads = dto.downloads,
            images = Images(preview = dto.preview, desktop = dto.desktop, mobile = dto.mobile),
            tags = dto.tags.split("#"),
            published = LocalDateTime.parse(dto.published)
        )
    }

}
