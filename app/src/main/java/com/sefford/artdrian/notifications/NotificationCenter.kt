package com.sefford.artdrian.notifications

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.content.Context
import android.content.res.Resources
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import com.sefford.artdrian.common.Permissions
import com.sefford.artdrian.common.di.Application
import com.sefford.artdrian.notifications.model.Channels
import com.sefford.artdrian.notifications.model.Notifications
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationCenter @Inject constructor(
    @Application private val context: Context,
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
    fun notify(notification: Notifications) {
        notifications.notify(
            notification.id,
            notification.toNotification(context, resources)
        )
    }

    fun clear(id: Int) = notifications.cancel(id)

    fun clearAll() = notifications.cancelAll()

    fun canNotifyOnChannel(channel: Channels) = notificationsEnabled &&
        notifications.getNotificationChannel(channel.id).enabled

    private val NotificationChannel?.enabled: Boolean
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this?.importance != NotificationManagerCompat.IMPORTANCE_NONE
        } else {
            true
        }
}
