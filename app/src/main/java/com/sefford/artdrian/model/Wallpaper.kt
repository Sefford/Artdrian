package com.sefford.artdrian.model

import com.sefford.artdrian.data.dto.MetadataDto
import com.sefford.artdrian.data.dto.isPngFile
import com.sefford.artdrian.model.Wallpaper.Extension.*
import java.util.*

data class Wallpaper(val metadataDto: MetadataDto) {
    val desktop: String
        get() = BASE_URL.format(Type.DESKTOP.identifier, metadataDto.slug, (if (metadataDto.isPngFile()) PNG else JPG).extension)
    val mobile: String
        get() = BASE_URL.format(Type.MOBILE.identifier, metadataDto.slug,  (if (metadataDto.isPngFile()) PNG else JPG).extension)
    val name: String
        get() = metadataDto.slug.split("_")
            .joinToString(separator = " ") { word ->
                if (word.toIntOrNull() == null) word.replaceFirstChar { it.uppercase(Locale.ENGLISH) } else "#${word}"
            }

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
