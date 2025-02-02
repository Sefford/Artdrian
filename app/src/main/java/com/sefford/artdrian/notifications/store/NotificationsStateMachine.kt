package com.sefford.artdrian.notifications.store

import com.sefford.artdrian.common.stores.StateMachine

val NotificationsStateMachine: StateMachine<NotificationEvents, NotificationsState, NotificationEffects> = { event ->
    when(event) {
        is NotificationEvents.Update -> {
            state { it + event.notification }
            effect(NotificationEffects.Notify(event.notification))
        }
        is NotificationEvents.Clear -> {
            state { it - event.id}
            effect(NotificationEffects.Clear(event.id))
        }
        NotificationEvents.ClearAll -> {
            state { it.clear() }
            effect(NotificationEffects.ClearAll)
        }
    }
}
