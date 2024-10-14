package com.sefford.artdrian.model

import com.sefford.artdrian.model.Wallpaper.Extension.JPG
import com.sefford.artdrian.model.Wallpaper.Extension.PNG
import java.util.Locale

data class Wallpaper(val metadata: Metadata) {
    val desktop: String
        get() = BASE_URL.format(Type.DESKTOP.identifier, metadata.slug, (if (metadata.isPngFile()) PNG else JPG).extension)
    val mobile: String
        get() = BASE_URL.format(Type.MOBILE.identifier, metadata.slug,  (if (metadata.isPngFile()) PNG else JPG).extension)
    val name: String
        get() = metadata.slug.split("_")
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
