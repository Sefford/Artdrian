package com.sefford.artdrian.downloads.store

import arrow.core.Option
import arrow.core.none
import arrow.core.toOption
import com.sefford.artdrian.common.data.DataError
import com.sefford.artdrian.common.language.files.Size
import com.sefford.artdrian.common.language.files.Size.Companion.bytes
import com.sefford.artdrian.downloads.domain.model.Download
import com.sefford.artdrian.downloads.domain.model.Downloads
import com.sefford.artdrian.downloads.domain.model.Measured
import com.sefford.artdrian.downloads.domain.model.plus

sealed class DownloadsState(val downloads: Downloads) {

    data object Idle : DownloadsState(emptyList()) {
        override fun viabilityOf(id: String): Viability = Viability.WAIT

        override fun plus(error: DataError): DownloadsState = Empty

        override fun plus(preloads: Preload): DownloadsState = preloads

        override fun get(url: String): Option<Download> = none()
    }

    data object Empty : DownloadsState(emptyList()) {

        override fun viabilityOf(id: String): Viability = Viability.FAILURE

        override fun plus(error: DataError): DownloadsState = this

        override fun plus(preloads: Preload): DownloadsState = Empty

        override fun get(url: String): Option<Download> = none()

    }

    class Preload(downloads: Downloads) : DownloadsState(downloads) {

        val empty: Boolean = downloads.isEmpty()

        override fun viabilityOf(id: String): Viability = Viability.WAIT

        override fun plus(error: DataError): DownloadsState = Loaded(downloads)

        override fun plus(preloads: Preload): DownloadsState = Preload((downloads + preloads.downloads).toSet().toList())

        override fun get(url: String): Option<Download> = downloads.find { it.url == url }.toOption()

        operator fun minus(other: Loaded): Downloads = downloads - other.downloads
    }

    class Loaded(downloads: Downloads) : DownloadsState(downloads), Measured {

        override fun viabilityOf(id: String): Viability = if (this[id].isNone()) Viability.FAILURE else Viability.PROCEED

        override val total: Size by lazy { downloads.sumOf { it.total } }

        override val progress: Size by lazy { downloads.sumOf { it.progress } }

        val pending: List<Download.Pending> = downloads.filterIsInstance<Download.Pending>()

        override fun plus(error: DataError): DownloadsState = this

        fun plus(list: Downloads): DownloadsState = Loaded(downloads + list)

        override fun plus(preloads: Preload): DownloadsState = Loaded(downloads)

        override fun get(url: String): Option<Download> = downloads.find { it.url == url }.toOption()

        override fun toString(): String {
            return "Loaded[${downloads.size}](downloads=$downloads)"
        }
    }

    operator fun minus(other: DownloadsState): Downloads = downloads - other.downloads

    abstract fun viabilityOf(id: String): Viability

    abstract operator fun plus(error: DataError): DownloadsState

    abstract operator fun plus(preloads: Preload): DownloadsState

    abstract operator fun get(url: String): Option<Download>

    enum class Viability { FAILURE, WAIT, PROCEED }
}

private inline fun <T> Iterable<T>.sumOf(selector: (T) -> Size): Size {
    var sum: Size = 0.bytes
    forEach { element -> sum += selector(element) }
    return sum
}
