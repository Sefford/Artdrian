package com.sefford.artdrian.data.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sefford.artdrian.model.Metadata

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

    constructor(metadata: Metadata) : this(
        id = metadata.id,
        title = metadata.title,
        views = metadata.views,
        downloads = metadata.downloads,
        slug = metadata.slug,
        created = metadata.created.toString(),
        updated = metadata.updated.toString()
    )
}
