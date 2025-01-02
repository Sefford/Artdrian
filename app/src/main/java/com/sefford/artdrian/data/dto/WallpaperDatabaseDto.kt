package com.sefford.artdrian.data.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sefford.artdrian.model.Wallpaper

@Entity(tableName = "wallpapers")
class WallpaperDatabaseDto(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "slug") val slug: String,
    @ColumnInfo(name = "version") val version: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "downloads") val downloads: Int,
    @ColumnInfo(name = "preview") val preview: String,
    @ColumnInfo(name = "desktop") val desktop: String,
    @ColumnInfo(name = "mobile") val mobile: String,
    @ColumnInfo(name = "tags") val tags: String,
    @ColumnInfo(name = "created") val published: String,
) {

    constructor(wallpaper: Wallpaper) : this(
        id = wallpaper.id,
        slug = wallpaper.slug,
        version = wallpaper.version,
        title = wallpaper.title,
        downloads = wallpaper.downloads,
        preview = wallpaper.images.preview,
        desktop = wallpaper.images.desktop,
        mobile = wallpaper.images.mobile,
        tags = wallpaper.tags.joinToString(separator = "#"),
        published = wallpaper.published.toString(),
    )
}
