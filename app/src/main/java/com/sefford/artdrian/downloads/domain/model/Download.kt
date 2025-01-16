package com.sefford.artdrian.downloads.domain.model

import com.sefford.artdrian.common.language.files.Size
import com.sefford.artdrian.common.language.files.Size.Companion.bytes
import com.sefford.artdrian.downloads.data.dto.DownloadDto

sealed class Download(
    val id: String,
    val url: String,
) {

    val finished: Boolean by lazy { order == Order.FINISHED.ordinal }

    class Pending(id: String, url: String) : Download(id, url) {

        constructor(url: String) : this(url.hashCode().toString(), url)

        override val order: Int = 0

        override fun toDto(): DownloadDto = DownloadDto(id, url)

        override fun plus(other: Download): Download = other

        fun prime(hash: String, name: String, total: Size) = Primed(id, url, hash, name, total)
    }

    class Primed(id: String, url: String, val hash: String, val name: String, override val total: Size) : Download(id, url),
        Measured {
        override val order: Int = 1

        override val progress: Size = 0.bytes

        override fun plus(other: Download): Download = if (this == other) {
            this
        } else if (order == other.order) {
            other
        } else {
            super.plus(other)
        }

        override fun toDto(): DownloadDto = DownloadDto(
            id = id,
            url = url,
            hash = hash,
            name = name,
            total = total.inBytes,
            downloaded = 0
        )

        fun start(uri: String) = Ongoing(id, url, hash, name, total, progress, uri)

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Primed) return false
            if (!super.equals(other)) return false

            if (hash != other.hash) return false
            if (total != other.total) return false

            return true
        }

        override fun hashCode(): Int {
            var result = super.hashCode()
            result = 31 * result + hash.hashCode()
            result = 31 * result + total.hashCode()
            return result
        }
    }

    class Ongoing(
        id: String,
        url: String,
        val hash: String,
        val name: String,
        override val total: Size,
        override val progress: Size,
        val uri: String
    ) :
        Download(id, url), Measured {

        override val order: Int = 2

        override fun toDto(): DownloadDto =
            DownloadDto(
                id = id,
                url = url,
                hash = hash,
                name = name,
                total = total.inBytes,
                downloaded = progress.inBytes,
                uri = uri
            )

        operator fun plus(downloaded: Long) = Ongoing(id, url, hash, name, total, progress + downloaded, uri)

        operator fun plus(size: Size) = Ongoing(id, url, hash, name, total, progress + size, uri)

        override fun plus(other: Download): Download = when {
            this == other && this < other as Ongoing -> other
            this > other -> this
            else -> super.plus(other)
        }

        override fun compareTo(other: Download): Int = if (order != other.order) {
            super.compareTo(other)
        } else {
            progress.compareTo((other as Ongoing).progress)
        }

        fun finish(): Finished {
            check(total == progress && total > 0 && progress > 0)
            return Finished(id, url, hash, name, total, uri)
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Ongoing) return false
            if (!super.equals(other)) return false

            if (hash != other.hash) return false
            if (total != other.total) return false
            if (progress != other.progress) return false

            return true
        }

        override fun hashCode(): Int {
            var result = super.hashCode()
            result = 31 * result + hash.hashCode()
            result = 31 * result + total.hashCode()
            result = 31 * result + progress.hashCode()
            return result
        }
    }

    class Finished(id: String, url: String, val hash: String, val name: String, override val total: Size, val uri: String) :
        Download(id, url), Measured {

        override val order: Int = 3

        override val progress: Size = total

        override fun plus(other: Download): Download = if (this == other) this else super.plus(other)

        override fun toDto(): DownloadDto = DownloadDto(
            id = id,
            url = url,
            hash = hash,
            name = name,
            total = total.inBytes,
            downloaded = total.inBytes,
            uri = uri
        )

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Finished) return false
            if (!super.equals(other)) return false

            if (hash != other.hash) return false
            if (total != other.total) return false

            return true
        }

        override fun hashCode(): Int {
            var result = super.hashCode()
            result = 31 * result + hash.hashCode()
            result = 31 * result + total.hashCode()
            return result
        }
    }

    protected abstract val order: Int

    open operator fun plus(other: Download): Download = takeIf { this > other } ?: other

    open operator fun compareTo(other: Download) = order.compareTo(other.order)

    abstract fun toDto(): DownloadDto

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Download) return false

        if (id != other.id) return false
        if (url != other.url) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + url.hashCode()
        return result
    }

    companion object {
        operator fun invoke(dto: DownloadDto): Download =
            when {
                dto.pending -> Pending(dto.id, dto.url)
                dto.primed -> Primed(dto.id, dto.url, dto.hash, dto.name, dto.total.bytes)
                dto.inProgress -> Ongoing(dto.id, dto.url, dto.hash, dto.name, dto.total.bytes, dto.downloaded.bytes, dto.uri)
                dto.completed -> Finished(dto.id, dto.url, dto.hash, dto.name, dto.total.bytes, dto.uri)
                else -> throw IllegalStateException("This is an unreachable condition")
            }
    }

    private enum class Order { PENDING, PRIMED, ONGOING, FINISHED }
}
