package com.sefford.artdrian.downloads.domain.model

import com.sefford.artdrian.common.language.files.Size
import com.sefford.artdrian.common.language.files.Size.Companion.bytes
import com.sefford.artdrian.downloads.data.dto.DownloadDto
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import okio.FileSystem
import okio.Path.Companion.toOkioPath
import okio.buffer
import okio.sink
import java.io.File

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

        fun start(headers: Headers, directory: File): Ongoing {
            val name = FILENAME_REGEX.find(headers[HttpHeaders.ContentDisposition]!!)?.groups?.get(1)?.value.orEmpty()
            return Ongoing(
                id = id,
                url = url,
                hash = headers[HttpHeaders.ETag]!!,
                name = name,
                total = headers[HttpHeaders.ContentLength]!!.toLong().bytes,
                file = File(directory, "$name.download")
            )
        }
    }

    class Ongoing(
        id: String,
        url: String,
        val hash: String,
        val name: String,
        override val total: Size,
        private val file: File
    ) : Download(id, url), Measured {

        override val order: Int = 2

        override val progress: Size
            get() = file.length().bytes

        val valid: Boolean
            get() = file.exists() && file.canWrite()

        val sink by lazy {
            file.sink(true).buffer()
        }

        override fun toDto(): DownloadDto =
            DownloadDto(
                id = id,
                url = url,
                hash = hash,
                name = name,
                total = total.inBytes,
                downloaded = progress.inBytes,
                uri = file.absolutePath
            )

        fun refresh(headers: Headers, destination: File) =
            if (hash != headers[HttpHeaders.ETag]) {
                Pending(id, url).start(headers, destination).also { clear() }
            } else {
                this
            }

        fun clear() {
            FileSystem.SYSTEM.write(file = file.toOkioPath()) {}
        }

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
            check(total == progress && total > 0 && progress > 0) { "Cannot complete the file: Status $progress/$total"}
            return Finished(id, url, hash, name, total, file)
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Ongoing) return false
            if (!super.equals(other)) return false

            if (hash != other.hash) return false
            if (name != other.name) return false
            if (total != other.total) return false

            return true
        }

        override fun hashCode(): Int {
            var result = super.hashCode()
            result = 31 * result + hash.hashCode()
            result = 31 * result + name.hashCode()
            result = 31 * result + total.hashCode()
            return result
        }
    }

    class Finished(id: String, url: String, val hash: String, val name: String, override val total: Size, val file: File) :
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
            uri = file.absolutePath
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
                dto.inProgress -> Ongoing(
                    dto.id,
                    dto.url,
                    dto.hash,
                    dto.name,
                    dto.total.bytes,
                    File(dto.uri)
                )

                dto.completed -> Finished(dto.id, dto.url, dto.hash, dto.name, dto.total.bytes, File(dto.uri))
                else -> throw IllegalStateException("This is an unreachable condition")
            }
    }

    private enum class Order { PENDING, PRIMED, ONGOING, FINISHED }

    protected val FILENAME_REGEX = """filename="([^"]+)"""".toRegex()
}
