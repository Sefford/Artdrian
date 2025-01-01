package com.sefford.artdrian.model

import com.sefford.artdrian.data.dto.WallpaperNetworkDto
import com.sefford.artdrian.data.dto.WallpaperDatabaseDto
import kotlinx.datetime.LocalDateTime
import java.util.Locale

class Metadata(
    val id: String,
    val title: String,
    val views: Int,
    val downloads: Int,
    val slug: String,
    val created: LocalDateTime,
    val updated: LocalDateTime = created,
) {

    constructor(dto: WallpaperNetworkDto) : this(
        id = dto.id,
        title = dto.title,
        views = 0,
        downloads = dto.downloads,
        slug = dto.slug,
        created = dto.created
    )

    constructor(dto: WallpaperDatabaseDto) : this(
        id = dto.id,
        title = dto.title,
        views = dto.views,
        downloads = dto.downloads,
        slug = dto.slug,
        created = LocalDateTime.parse(dto.created),
        updated = LocalDateTime.parse(dto.updated)
    )

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

    private companion object {
        val BASE_URL = "https://adrianmato.art/static/downloads/%s/%s.%s"
    }

    private enum class Type(val identifier: String) {
        DESKTOP("desktop"),
        MOBILE("mobile")
    }

    private enum class Extension(val extension: String) {
        PNG("png"),
        JPG("jpg")
    }
}
