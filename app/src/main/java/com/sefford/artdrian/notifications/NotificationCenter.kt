package com.sefford.artdrian.notifications

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.content.Context
import android.content.res.Resources
import android.os.Build
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

    val notificationsEnabled: Boolean
        get() = permissions.notifications

    @SuppressLint("MissingPermission")
    fun notify(notification: Notifications.DownloadNotification) {
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

    fun canNotifyOnChannel(channel: Channels) = notificationsEnabled &&
        notifications.getNotificationChannel(channel.id).enabled

    private val NotificationChannel?.enabled: Boolean
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this?.importance == NotificationManagerCompat.IMPORTANCE_NONE
        } else {
            true
        }

    fun clearDownloadNotification() {
        notifications.cancel(DOWNLOADS_ID)
    }

    private val DOWNLOADS_ID = 310520
}
