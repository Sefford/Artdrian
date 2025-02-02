package com.sefford.artdrian.notifications.store

import com.sefford.artdrian.common.stores.KotlinStore

typealias NotificationsStore = KotlinStore<NotificationEvents, NotificationsState, NotificationEffects>
