package com.sefford.artdrian.notifications

import android.content.Context
import android.content.res.Resources
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.sefford.artdrian.R
import com.sefford.artdrian.notifications.model.Channels
import com.sefford.artdrian.notifications.model.Notifications

class NotificationCenter(
    private val context: Context,
    private val resources: Resources,
    private val notifications: NotificationManagerCompat
) {

    init {
        notifications.createNotificationChannelsCompat(Channels(resources))
    }

    fun notify(notification: Notifications.DownloadNotification) {
        NotificationCompat.Builder(context, Channels.DOWNLOAD.id)
            .setContentTitle(resources.getString(R.string.notification_download_title))
            .setContentText(resources.getString(R.string.notification_download_description))
            .setSmallIcon(R.drawable.ic_notification)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setProgress(notification.total.toInt(), notification.progress.toInt(), false)
            .build()

        notifications.areNotificationsEnabled()

    }
}
