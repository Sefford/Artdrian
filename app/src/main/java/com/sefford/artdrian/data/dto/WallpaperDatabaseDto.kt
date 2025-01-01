package com.sefford.artdrian.data.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sefford.artdrian.model.Wallpaper

@Entity(tableName = "wallpapers")
class WallpaperDatabaseDto(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "views") val views: Int,
    @ColumnInfo(name = "downloads") val downloads: Int,
    @ColumnInfo(name = "slug") val slug: String,
    @ColumnInfo(name = "created") val created: String,
    @ColumnInfo(name = "updated") val updated: String
) {

    constructor(wallpaper: Wallpaper) : this(
        id = wallpaper.id,
        title = wallpaper.title,
        views = wallpaper.views,
        downloads = wallpaper.downloads,
        slug = wallpaper.slug,
        created = wallpaper.created.toString(),
        updated = wallpaper.updated.toString()
    )
}
