package com.sefford.artdrian.notifications.model

import android.content.res.Resources
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationManagerCompat
import com.sefford.artdrian.R

enum class Channels(
    val id: String,
    val importance: Int
) {

    GENERAL("general", NotificationManagerCompat.IMPORTANCE_MIN) {
        override fun toChannel(resources: Resources): NotificationChannelCompat =
            NotificationChannelCompat.Builder(id, importance)
                .setName(resources.getString(R.string.notification_channel_general_name))
                .setDescription(resources.getString(R.string.notification_channel_general_description))
                .build()

    },
    DOWNLOAD("downloads", NotificationManagerCompat.IMPORTANCE_MIN) {
        override fun toChannel(resources: Resources): NotificationChannelCompat =
            NotificationChannelCompat.Builder("downloads", NotificationManagerCompat.IMPORTANCE_MIN)
                .setName(resources.getString(R.string.notification_channel_downloads_name))
                .setDescription(resources.getString(R.string.notification_channel_downloads_description))
                .build()
    };

    abstract fun toChannel(resources: Resources): NotificationChannelCompat

    companion object {
        operator fun invoke(resources: Resources) = entries.map { it.toChannel(resources) }
    }
}
