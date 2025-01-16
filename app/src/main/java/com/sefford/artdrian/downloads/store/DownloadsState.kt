package com.sefford.artdrian.downloads.store

import com.sefford.artdrian.common.data.DataError
import com.sefford.artdrian.common.language.files.Size
import com.sefford.artdrian.common.language.files.Size.Companion.bytes
import com.sefford.artdrian.downloads.domain.model.Download
import com.sefford.artdrian.downloads.domain.model.Downloads
import com.sefford.artdrian.downloads.domain.model.Measured

sealed class DownloadsState {

    data object Idle : DownloadsState() {
        override fun plus(error: DataError): DownloadsState = Empty

        override fun plus(list: Downloads): DownloadsState = if (list.isNotEmpty()) Loaded(list) else Empty

        override fun plus(preloads: Preload): DownloadsState = if (preloads.empty) Idle else preloads
    }

    data object Empty : DownloadsState() {
        override fun plus(error: DataError): DownloadsState = this

        override fun plus(list: Downloads): DownloadsState = if (list.isNotEmpty()) Loaded(list) else Empty

        override fun plus(preloads: Preload): DownloadsState = if (!preloads.empty) Loaded(preloads.downloads) else Empty
    }

    class Preload(val downloads: Downloads) : DownloadsState() {
        val empty: Boolean = downloads.isEmpty()

        override fun plus(error: DataError): DownloadsState = Loaded(downloads)

        override fun plus(list: Downloads): DownloadsState =
            Loaded(downloads + list)

        override fun plus(preloads: Preload): DownloadsState = Preload(downloads + preloads.downloads)
    }

    class Loaded(val downloads: Downloads) : DownloadsState(), Measured {

        override val total: Size by lazy { downloads.filterIsInstance<Measured>().sumOf { it.total } }

        override val progress: Size by lazy { downloads.filterIsInstance<Measured>().sumOf { it.progress } }

        val pending: List<Download.Pending> = downloads.filterIsInstance<Download.Pending>()

        override fun plus(error: DataError): DownloadsState = this

        override fun plus(list: Downloads): DownloadsState = Loaded(downloads + list)

        override fun plus(preloads: Preload): DownloadsState = Loaded(downloads + preloads.downloads)
    }

    abstract operator fun plus(error: DataError): DownloadsState

    abstract operator fun plus(list: Downloads): DownloadsState

    abstract operator fun plus(preloads: Preload): DownloadsState
}

private inline fun <T> Iterable<T>.sumOf(selector: (T) -> Size): Size {
    var sum: Size = 0.bytes
    forEach { element -> sum += selector(element) }
    return sum
}
