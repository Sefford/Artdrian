package com.sefford.artdrian.downloads.domain.model

import com.sefford.artdrian.common.language.files.Size
import com.sefford.artdrian.common.language.files.Size.Companion.bytes
import com.sefford.artdrian.common.language.removeQuotes
import com.sefford.artdrian.downloads.data.dto.DownloadDto
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import okio.FileSystem
import okio.Path.Companion.toOkioPath
import okio.buffer
import okio.sink
import java.io.File

sealed class Download(val url: String) : Measured {

    val format: Format by lazy { Format(url) }

    val finished: Boolean by lazy { order == Order.FINISHED.ordinal }

    class Pending(url: String) : Download(url) {

        override val order: Int = Order.PENDING.ordinal

        override val progress: Size = 0.bytes

        override val total: Size = 0.bytes

        override fun toDto(): DownloadDto = DownloadDto(url)

        fun start(headers: Headers, directory: File): Ongoing {
            val name = FILENAME_REGEX.find(headers[HttpHeaders.ContentDisposition]!!)?.groups?.get(1)?.value.orEmpty()
            return Ongoing(
                url = url,
                hash = headers[HttpHeaders.ETag]!!.removeQuotes(),
                name = name,
                total = headers[HttpHeaders.ContentLength]!!.toLong().bytes,
                file = File(directory, "$name.${format.suffix}.download")
            )
        }

        override fun toString(): String {
            return "Pending($url)"
        }
    }

    class Ongoing(
        url: String,
        val hash: String,
        val name: String,
        override val total: Size,
        private val file: File
    ) : Download(url) {

        override val order: Int = Order.ONGOING.ordinal

        override val progress: Size
            get() = file.length().bytes

        val valid: Boolean
            get() = file.exists() && file.canWrite()

        val sink by lazy {
            file.sink(true).buffer()
        }

        val uri: String = file.absolutePath

        val fileName: String = file.name

        override fun toDto(): DownloadDto =
            DownloadDto(
                url = url,
                hash = hash,
                name = name,
                total = total.inBytes,
                downloaded = progress.inBytes,
                uri = file.absolutePath
            )

        fun refresh(headers: Headers, destination: File) =
            if (hash != headers[HttpHeaders.ETag]?.removeQuotes()) {
                Pending(url).start(headers, destination).also { clear() }
            } else {
                this
            }

        fun clear() {
            if (!file.exists()) {
                file.parentFile?.mkdirs()
                file.createNewFile()
            }
            FileSystem.SYSTEM.write(file = file.toOkioPath()) {}
        }

        override fun compareTo(other: Download): Int = if (order != other.order) {
            super.compareTo(other)
        } else {
            progress.compareTo((other as Ongoing).progress)
        }

        fun finish(): Finished {
            check(total == progress && total > 0 && progress > 0) { "Cannot complete the file: Status $progress/$total" }
            return Finished(url, hash, name, total, file)
        }


        override fun toString(): String {
            return "Ongoing($url)"
        }

        override fun equals(other: Any?): Boolean {
            if (other is Ongoing) return super.equals(other) && hash == other.hash
            return super.equals(other)
        }
    }

    class Finished(
        url: String,
        val hash: String,
        val name: String,
        override val total: Size,
        val file: File
    ) : Download(url) {

        override val order: Int = Order.FINISHED.ordinal

        override val progress: Size = total

        val uri: String = file.absolutePath

        val fileName: String = file.name

        override fun toDto(): DownloadDto = DownloadDto(
            url = url,
            hash = hash,
            name = name,
            total = total.inBytes,
            downloaded = total.inBytes,
            uri = file.absolutePath
        )

        override fun toString(): String {
            return "Finished($url)"
        }
    }

    protected abstract val order: Int

    operator fun plus(other: Download): Download {
        check(this == other) { "Cannot merge two different downloads!" }
        return takeIf { this > other } ?: other
    }

    open operator fun compareTo(other: Download) = order.compareTo(other.order)

    abstract fun toDto(): DownloadDto

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Download) return false

        if (url != other.url) return false

        return true
    }

    override fun hashCode(): Int {
        return url.hashCode()
    }

    companion object {
        operator fun invoke(dto: DownloadDto): Download =
            when {
                dto.pending -> Pending(dto.url)
                dto.ongoing -> Ongoing(
                    dto.url,
                    dto.hash,
                    dto.name,
                    dto.total.bytes,
                    File(dto.uri)
                )

                dto.completed -> Finished(
                    dto.url,
                    dto.hash,
                    dto.name,
                    dto.total.bytes,
                    File(dto.uri)
                )

                else -> throw IllegalStateException("This is an unreachable condition")
            }
    }

    private enum class Order { PENDING, ONGOING, FINISHED }

    enum class Format(val suffix: String) {
        DESKTOP("desktop"), MOBILE("mobile"), UNKNOWN("unknown");

        companion object {
            operator fun invoke(format: String) =
                FORMAT_REGEX.find(format)?.groupValues?.get(1).let { value ->
                    when (value) {
                        "desktop" -> DESKTOP
                        "mobile" -> MOBILE
                        else -> UNKNOWN

                    }
                }

            private val FORMAT_REGEX = "(mobile|desktop)".toRegex()
        }
    }

    protected val FILENAME_REGEX = """filename="([^"]+)"""".toRegex()
}
