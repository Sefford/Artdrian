package com.sefford.artdrian.notifications.model

import android.app.Notification
import android.content.Context
import android.content.res.Resources
import android.util.Log
import androidx.core.app.NotificationCompat
import com.sefford.artdrian.R
import com.sefford.artdrian.common.language.files.Size
import com.sefford.artdrian.common.language.files.Size.Companion.bytes
import com.sefford.artdrian.downloads.domain.model.Download
import com.sefford.artdrian.downloads.domain.model.Downloads
import com.sefford.artdrian.downloads.domain.model.Measured
import com.sefford.artdrian.downloads.domain.model.plus
import com.sefford.artdrian.downloads.domain.model.progress
import com.sefford.artdrian.downloads.domain.model.total

sealed class Notifications(val id: Int) {

    class DownloadNotification(
        private val downloads: Downloads
    ) : Notifications(DOWNLOADS_ID), Measured {
        override val total: Size
            get() = downloads.total.bytes
        override val progress: Size
            get() = downloads.progress.bytes
        val number: Int = downloads.size
        val indeterminate: Boolean
            get() = downloads.all { download -> download is Download.Pending }

        operator fun plus(notification: DownloadNotification) =
            DownloadNotification(downloads + notification.downloads)

        override fun toNotification(context: Context, resources: Resources) =
            NotificationCompat.Builder(context, Channels.DOWNLOAD.id)
                .setContentTitle(resources.getString(R.string.notification_download_title))
                .setContentText(resources.getString(R.string.notification_download_description, number))
                .setSmallIcon(R.drawable.ic_notification)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setProgress(
                    total.inBytes.toInt(),
                    progress.inBytes.toInt(),
                    indeterminate
                )
                .build()
    }

    abstract fun toNotification(context: Context, resources: Resources): Notification

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Notifications

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    companion object {
        const val DOWNLOADS_ID = 310520
    }

}
