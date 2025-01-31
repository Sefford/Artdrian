package com.sefford.artdrian.notifications

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.sefford.artdrian.R
import com.sefford.artdrian.common.Permissions
import com.sefford.artdrian.notifications.model.Channels
import com.sefford.artdrian.notifications.model.Notifications

class NotificationCenter(
    private val context: Context,
    private val resources: Resources,
    private val notifications: NotificationManagerCompat,
    private val permissions: Permissions,
) {

    init {
        notifications.createNotificationChannelsCompat(Channels(resources))
    }

    @SuppressLint("MissingPermission")
    fun notify(notification: Notifications.DownloadNotification) {
        if (permissions.notifications) {
            notifications.notify(
                DOWNLOADS_ID,
                NotificationCompat.Builder(context, Channels.DOWNLOAD.id)
                    .setContentTitle(resources.getString(R.string.notification_download_title))
                    .setContentText(resources.getString(R.string.notification_download_description, notification.downloads))
                    .setSmallIcon(R.drawable.ic_notification)
                    .setOngoing(true)
                    .setOnlyAlertOnce(true)
                    .setProgress(notification.total.toInt(), notification.progress.toInt(), notification.total == 0L)
                    .build()
            )
        }
    }

    fun clearDownloadNotification() {
        notifications.cancel(DOWNLOADS_ID)
    }

    private val DOWNLOADS_ID = 310520
}
