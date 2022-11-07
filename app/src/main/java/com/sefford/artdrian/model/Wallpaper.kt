package com.sefford.artdrian.model

data class Wallpaper(val metadata: Metadata) {
    val desktop: String
        get() = BASE_URL.format(Type.DESKTOP.identifier, metadata.slug)
    val mobile: String
        get() = BASE_URL.format(Type.MOBILE.identifier, metadata.slug)

    companion object {
        val BASE_URL = "https://adrianmato.art/static/downloads/%s/%s.png"
    }

    enum class Type(val identifier: String) {
        DESKTOP("desktop"),
        MOBILE("mobile")
    }
}
