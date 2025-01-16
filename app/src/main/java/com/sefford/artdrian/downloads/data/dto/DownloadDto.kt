package com.sefford.artdrian.downloads.data.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sefford.artdrian.downloads.domain.model.Download

@Entity(tableName = "downloads")
class DownloadDto(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "hash") val hash: String = "",
    @ColumnInfo(name = "name") val name: String = "",
    @ColumnInfo(name = "total") val total: Long = -1L,
    @ColumnInfo(name = "downloaded") val downloaded: Long = -1L,
    @ColumnInfo(name = "uri") val uri: String = ""
) {

    @Transient
    val pending: Boolean = hash.isEmpty()

    @Transient
    val primed: Boolean = hash.isNotEmpty() && total > 0L && downloaded == 0L

    @Transient
    val inProgress: Boolean = hash.isNotEmpty() && total > -1L && total > downloaded

    @Transient
    val completed: Boolean = hash.isNotEmpty() && total > -1L && total == downloaded

    fun toDomain() = Download(this)
}
