package com.sefford.artdrian.model

import com.sefford.artdrian.Endpoints.HOST
import com.sefford.artdrian.data.dto.WallpaperNetworkDto

class Images(
    val preview: String,
    val desktop: String,
    val mobile: String,
) {
    constructor(dto: WallpaperNetworkDto) : this(
        preview = "$HOST${dto.preview}",
        desktop = BASE_URL.format(Type.DESKTOP.identifier, dto.slug, dto.extension),
        mobile = BASE_URL.format(Type.MOBILE.identifier, dto.slug, dto.extension),
    )

    private enum class Type(val identifier: String) {
        DESKTOP("desktop"),
        MOBILE("mobile")
    }
}

private const val BASE_URL = "$HOST/static/downloads/%s/%s.%s"
