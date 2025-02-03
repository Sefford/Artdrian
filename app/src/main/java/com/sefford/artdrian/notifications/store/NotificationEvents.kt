package com.sefford.artdrian.notifications.store

import com.sefford.artdrian.notifications.model.Notifications

sealed class NotificationEvents {

    class Update(val notification: Notifications) : NotificationEvents()

    class Clear(val id: Int) : NotificationEvents()

    data object ClearAll : NotificationEvents()

}
