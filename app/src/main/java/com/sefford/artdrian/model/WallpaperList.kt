package com.sefford.artdrian.model

class WallpaperList(val wallpapers: List<Metadata>, val source: Source) {

    constructor(wallpaper: Wallpaper): this(listOf(wallpaper.metadata), wallpaper.source)

    constructor(metadata: Metadata, source: Source): this(Wallpaper(metadata, source))
}
