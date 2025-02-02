package com.sefford.artdrian.notifications.store

import com.sefford.artdrian.notifications.model.Notifications

class NotificationsState(
    val notifications: Set<Notifications> = emptySet()
) {

    constructor(notification: Notifications): this(setOf(notification))

    operator fun plus(notification: Notifications) = NotificationsState(notifications + notification)

    operator fun minus(notification: Notifications) = NotificationsState(notifications - notification)

    operator fun minus(id: Int): NotificationsState = NotificationsState(notifications.filterNot { it.id == id }.toSet())

    fun clear() = NotificationsState(emptySet())

}
