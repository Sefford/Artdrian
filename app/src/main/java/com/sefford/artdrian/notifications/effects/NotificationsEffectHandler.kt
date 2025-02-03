package com.sefford.artdrian.notifications.effects

import com.sefford.artdrian.notifications.NotificationCenter
import com.sefford.artdrian.notifications.model.Notifications
import com.sefford.artdrian.notifications.store.NotificationEffects
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class NotificationsEffectHandler(
    private val notify: (Notifications) -> Unit,
    private val clear: (Int) -> Unit,
    private val clearAll: () -> Unit,
    private val scope: CoroutineScope
) {

    constructor(notifications: NotificationCenter, scope: CoroutineScope) :
        this(notifications::notify, notifications::clear, notifications::clearAll, scope)

    fun handle(effect: NotificationEffects) = scope.launch {
        when (effect) {
            is NotificationEffects.Notify -> notify(effect.notification)
            is NotificationEffects.Clear -> clear(effect.id)
            NotificationEffects.ClearAll -> clearAll()
        }
    }
}
