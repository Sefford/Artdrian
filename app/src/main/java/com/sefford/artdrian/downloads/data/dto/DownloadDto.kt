package com.sefford.artdrian.downloads.data.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sefford.artdrian.downloads.domain.model.Download

@Entity(tableName = "downloads")
class DownloadDto(
    @PrimaryKey val url: String,
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
    val ongoing: Boolean = hash.isNotEmpty() && total > -1L && total > downloaded

    @Transient
    val completed: Boolean = hash.isNotEmpty() && total > -1L && total == downloaded

    fun toDomain() = Download(this)


    override fun toString(): String {
        return "DownloadDto(url='$url', hash='$hash', name='$name', total=$total, downloaded=$downloaded, uri='$uri', pending=$pending, primed=$primed, inProgress=$ongoing, completed=$completed)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DownloadDto

        if (url != other.url) return false
        if (hash != other.hash) return false
        if (name != other.name) return false
        if (total != other.total) return false
        if (downloaded != other.downloaded) return false
        if (uri != other.uri) return false

        return true
    }

    override fun hashCode(): Int {
        var result = url.hashCode()
        result = 31 * result + hash.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + total.hashCode()
        result = 31 * result + downloaded.hashCode()
        result = 31 * result + uri.hashCode()
        return result
    }
}
