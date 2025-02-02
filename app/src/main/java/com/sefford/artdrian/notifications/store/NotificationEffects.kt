package com.sefford.artdrian.notifications.store

import com.sefford.artdrian.notifications.model.Notifications

sealed class NotificationEffects {

    class Notify(val notification: Notifications) : NotificationEffects()

    class Clear(val id: Int) : NotificationEffects()

    data object ClearAll: NotificationEffects()

}
