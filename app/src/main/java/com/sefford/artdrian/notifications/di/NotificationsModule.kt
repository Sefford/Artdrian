package com.sefford.artdrian.notifications.di

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import com.sefford.artdrian.common.Permissions
import com.sefford.artdrian.common.di.Application
import com.sefford.artdrian.common.di.Default
import com.sefford.artdrian.downloads.store.DownloadsStore
import com.sefford.artdrian.notifications.NotificationCenter
import com.sefford.artdrian.notifications.bridgeNotifications
import com.sefford.artdrian.notifications.model.Channels
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
class NotificationsModule {

    @Provides
    @Singleton
    fun provideNotificationCenter(
        @Application context: Context,
        notifications: NotificationManagerCompat,
        downloads: DownloadsStore,
        permissions: Permissions,
        @Default scope: CoroutineScope,
    ) = NotificationCenter(context, context.resources, notifications, permissions).also { center ->
        downloads.state.bridgeNotifications(
            { center.canNotifyOnChannel(Channels.DOWNLOAD) },
            center::notify,
            scope
        )
    }

}
