package com.sefford.artdrian.model

import java.util.*

data class Wallpaper(val metadata: Metadata) {
    val desktop: String
        get() = BASE_URL.format(Type.DESKTOP.identifier, metadata.slug)
    val mobile: String
        get() = BASE_URL.format(Type.MOBILE.identifier, metadata.slug)
    val name: String
        get() = metadata.slug.split("_")
            .joinToString(separator = " ") { word ->
                if (word.toIntOrNull() == null) word.replaceFirstChar { it.uppercase(Locale.ENGLISH) } else "#${word}"
            }

    private companion object {
        val BASE_URL = "https://adrianmato.art/static/downloads/%s/%s.png"
    }

    private enum class Type(val identifier: String) {
        DESKTOP("desktop"),
        MOBILE("mobile")
    }
}
