package com.sefford.artdrian.notifications.di

import com.sefford.artdrian.common.di.Default
import com.sefford.artdrian.common.stores.KotlinStore
import com.sefford.artdrian.notifications.NotificationCenter
import com.sefford.artdrian.notifications.effects.NotificationsEffectHandler
import com.sefford.artdrian.notifications.effects.bridgeEffectHandler
import com.sefford.artdrian.notifications.store.NotificationsState
import com.sefford.artdrian.notifications.store.NotificationsStateMachine
import com.sefford.artdrian.notifications.store.NotificationsStore
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
class NotificationsModule {

    @Provides
    @Singleton
    fun provideNotificationsEffectHandler(
        notifications: NotificationCenter,
        @Default scope: CoroutineScope,
    ) = NotificationsEffectHandler(notifications, scope)

    @Provides
    @Singleton
    fun provideNotificationsStore(
        effectHandler: NotificationsEffectHandler,
        @Default scope: CoroutineScope,
    ): NotificationsStore = KotlinStore(NotificationsStateMachine, NotificationsState(), scope).also { store ->
        store.bridgeEffectHandler(effectHandler, scope)
    }

}
