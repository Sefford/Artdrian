package com.sefford.artdrian.notifications.model

import com.sefford.artdrian.downloads.domain.model.Download
import com.sefford.artdrian.downloads.domain.model.Downloads
import com.sefford.artdrian.downloads.domain.model.filterFinished
import com.sefford.artdrian.downloads.domain.model.plus

sealed class NotificationsBridgeState {
    data object Idle : NotificationsBridgeState() {
        override fun plus(other: NotificationsBridgeState) = other

        override val finished: Boolean = false
    }

    class Notifying private constructor(val downloads: Downloads) : NotificationsBridgeState() {
        override fun plus(other: NotificationsBridgeState): NotificationsBridgeState =
            when (other) {
                is Notifying -> merge(other.downloads)
                Refreshing -> merge(emptySet())
                Finished, Idle -> this
            }

        private fun merge(other: Downloads): NotificationsBridgeState =
            Notifying(other + downloads).takeIf { !it.finished } ?: Finished

        override val finished: Boolean by lazy { downloads.all { download -> download.finished } }

        companion object {
            operator fun invoke(downloads: Downloads) = downloads.filterFinished()
                .takeIf { it.isNotEmpty() }
                ?.let { Notifying(it) }
                ?: Finished

            operator fun invoke(download: Download) = Notifying(setOf(download))
        }

    }

    data object Refreshing : NotificationsBridgeState() {
        override fun plus(other: NotificationsBridgeState): NotificationsBridgeState = other

        override val finished: Boolean = false

    }

    data object Finished : NotificationsBridgeState() {
        override fun plus(other: NotificationsBridgeState) = other

        override val finished: Boolean = true
    }

    abstract operator fun plus(other: NotificationsBridgeState): NotificationsBridgeState

    abstract val finished: Boolean
}
