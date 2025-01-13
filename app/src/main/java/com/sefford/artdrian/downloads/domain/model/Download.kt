package com.sefford.artdrian.downloads.domain.model

import com.sefford.artdrian.downloads.data.dto.DownloadDto

sealed class Download(
    val id: String,
    val url: String,
) {

    val finished: Boolean by lazy { order == Order.FINISHED.ordinal }

    class Pending(id: String, url: String) : Download(id, url) {
        override val order: Int = 0

        override fun toDto(): DownloadDto = DownloadDto(id, url)

        override fun plus(other: Download): Download = other

        fun prime(hash: String, total: Long) = Primed(id, url, hash, total)
    }

    class Primed(id: String, url: String, val hash: String, override val total: Long) : Download(id, url),
        Measured {
        override val order: Int = 1

        override val progress: Long = 0

        override fun plus(other: Download): Download = if (this == other) {
            this
        } else if (order == other.order) {
            other
        } else {
            super.plus(other)
        }

        override fun toDto(): DownloadDto = DownloadDto(id, url, hash = hash, total = total, downloaded = 0)

        fun start(uri: String) = Ongoing(id, url, hash, total, progress, uri)

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
        override val total: Long,
        override val progress: Long,
        val uri: String
    ) :
        Download(id, url), Measured {

        override val order: Int = 2

        override fun toDto(): DownloadDto = DownloadDto(id, url, hash = hash, total = total, downloaded = progress, uri = uri)

        operator fun plus(downloaded: Long) = Ongoing(
            id, url, hash, total, progress + downloaded, uri
        )

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
            return Finished(id, url, hash, total, uri)
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

    class Finished(id: String, url: String, val hash: String, override val total: Long, val uri: String) :
        Download(id, url), Measured {

        override val order: Int = 3

        override val progress: Long = total

        override fun plus(other: Download): Download = if (this == other) this else super.plus(other)

        override fun toDto(): DownloadDto = DownloadDto(id, url, hash = hash, total = total, downloaded = total, uri = uri)

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
                dto.primed -> Primed(dto.id, dto.url, dto.hash, dto.total)
                dto.inProgress -> Ongoing(dto.id, dto.url, dto.hash, dto.total, dto.downloaded, dto.uri)
                dto.completed -> Finished(dto.id, dto.url, dto.hash, dto.total, dto.uri)
                else -> throw IllegalStateException("This is an unreachable condition")
            }
    }

    private enum class Order { PENDING, PRIMED, ONGOING, FINISHED }
}
