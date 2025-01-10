package com.sefford.artdrian.downloads.store

import com.sefford.artdrian.common.data.DataError
import com.sefford.artdrian.downloads.domain.model.Download
import com.sefford.artdrian.downloads.domain.model.Measured

sealed class DownloadsState {

    data object Idle : DownloadsState() {
        override fun plus(error: DataError): DownloadsState = Empty

        override fun plus(list: List<Download>): DownloadsState = Loaded(list)
    }

    data object Empty : DownloadsState() {
        override fun plus(error: DataError): DownloadsState = this

        override fun plus(list: List<Download>): DownloadsState = Loaded(list)
    }

    class Loaded(val list: List<Download>) : DownloadsState(), Measured {

        override val total: Long by lazy { list.filterIsInstance<Measured>().sumOf { total } }

        override val progress: Long by lazy { list.filterIsInstance<Measured>().sumOf { progress } }

        override fun plus(error: DataError): DownloadsState = this

        override fun plus(list: List<Download>): DownloadsState {
            TODO("Not yet implemented")
        }

    }

    abstract operator fun plus(error: DataError): DownloadsState

    abstract operator fun plus(list: List<Download>): DownloadsState

}
