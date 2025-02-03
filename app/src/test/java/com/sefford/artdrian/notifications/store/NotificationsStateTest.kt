package com.sefford.artdrian.notifications.store

import com.sefford.artdrian.test.mothers.DownloadsMother
import com.sefford.artdrian.test.mothers.NotificationMother
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainOnly
import org.junit.jupiter.api.Test

class NotificationsStateTest {

    @Test
    fun `adds a notification`() {
        (NotificationsState() + NotificationMother.createDownloadNotification())
            .notifications.shouldContainOnly(NotificationMother.createDownloadNotification())
    }

    @Test
    fun `updates a notification`() {
        (NotificationsState(
            NotificationMother.createDownloadNotification(downloads = setOf(DownloadsMother.createPending()))
        ) + NotificationMother.createDownloadNotification())
            .notifications.shouldContainOnly(NotificationMother.createDownloadNotification())
    }

    @Test
    fun `clears a notification`() {
        (NotificationsState(NotificationMother.createDownloadNotification()) - NotificationMother.createDownloadNotification())
            .notifications.shouldBeEmpty()
    }

    @Test
    fun `clears a notification via an ID`() {
        (NotificationsState(NotificationMother.createDownloadNotification()) - NotificationMother.createDownloadNotification().id)
            .notifications.shouldBeEmpty()
    }
}
