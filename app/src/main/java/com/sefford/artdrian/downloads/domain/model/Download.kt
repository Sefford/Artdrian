package com.sefford.artdrian.downloads.domain.model

import com.sefford.artdrian.downloads.data.dto.DownloadDto

sealed class Download(
    val id: String,
    val url: String,
) {

    class Pending(id: String, url: String) : Download(id, url) {

        override fun toDto(): DownloadDto = DownloadDto(id, url)

        fun prime(hash: String, total: Long) = Primed(id, url, hash, total)
    }

    class Primed(id: String, url: String, val hash: String, override val total: Long) : Download(id, url), Measured {
        override val progress: Long = 0

        operator fun plus(other: Primed) = other

        override fun toDto(): DownloadDto = DownloadDto(id, url, hash = hash, total = total, downloaded = 0)

        fun start(uri: String) = InProgress(id, url, hash, total, progress, uri)

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Primed) return false
            if (!super.equals(other)) return false

            if (hash != other.hash) return false

            return true
        }

        override fun hashCode(): Int {
            var result = super.hashCode()
            result = 31 * result + hash.hashCode()
            return result
        }
    }

    class InProgress(
        id: String,
        url: String,
        val hash: String,
        override val total: Long,
        override val progress: Long,
        val uri: String
    ) :
        Download(id, url), Measured {

        override fun toDto(): DownloadDto = DownloadDto(id, url, hash = hash, total = total, downloaded = progress, uri = uri)

        operator fun plus(downloaded: Long) = InProgress(
            id, url, hash, total, progress + downloaded, uri
        )

        fun finish(): Finished {
            check(total == progress && total > 0 && progress > 0)
            return Finished(id, url, hash, total, uri)
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Primed) return false
            if (!super.equals(other)) return false

            if (hash != other.hash) return false

            return true
        }

        override fun hashCode(): Int {
            var result = super.hashCode()
            result = 31 * result + hash.hashCode()
            return result
        }
    }

    class Finished(id: String, url: String, val hash: String, override val total: Long, val uri: String) : Download(id, url),
        Measured {

        override val progress: Long = total

        override fun toDto(): DownloadDto = DownloadDto(id, url, hash = hash, total = total, downloaded = total, uri = uri)

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Primed) return false
            if (!super.equals(other)) return false

            if (hash != other.hash) return false

            return true
        }

        override fun hashCode(): Int {
            var result = super.hashCode()
            result = 31 * result + hash.hashCode()
            return result
        }
    }

    abstract fun toDto(): DownloadDto

    companion object {
        operator fun invoke(dto: DownloadDto): Download =
            when {
                dto.pending -> Pending(dto.id, dto.url)
                dto.primed -> Primed(dto.id, dto.url, dto.hash, dto.total)
                dto.inProgress -> InProgress(dto.id, dto.url, dto.hash, dto.total, dto.downloaded, dto.uri)
                dto.completed -> Finished(dto.id, dto.url, dto.hash, dto.total, dto.uri)
                else -> throw IllegalStateException("This is an unreachable condition")
            }
    }
}
