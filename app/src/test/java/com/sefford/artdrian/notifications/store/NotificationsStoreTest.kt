package com.sefford.artdrian.notifications.store

import com.sefford.artdrian.test.StoreInstrumentation
import com.sefford.artdrian.test.mothers.NotificationMother
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainOnly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.jupiter.api.Test

class NotificationsStoreTest {

    @Test
    fun `adds a notification`() {
        val store = StoreInstrumentation(NotificationsStateMachine, NotificationsState())

        store.event(NotificationEvents.Update(NotificationMother.createDownloadNotification()))

        store.result.should { (states, effects) ->
            states.last().notifications.shouldContainOnly(NotificationMother.createDownloadNotification())
            effects.shouldHaveSize(1).last().shouldBeInstanceOf<NotificationEffects.Notify>()
                .notification shouldBe NotificationMother.createDownloadNotification()
        }
    }

    @Test
    fun `clears a notification`() {
        val store = StoreInstrumentation(NotificationsStateMachine, NotificationsState(NotificationMother.createDownloadNotification()))

        store.event(NotificationEvents.Clear(NotificationMother.createDownloadNotification().id))

        store.result.should { (states, effects) ->
            states.last().notifications.shouldBeEmpty()
            effects.shouldHaveSize(1).last().shouldBeInstanceOf<NotificationEffects.Clear>()
                .id shouldBe NotificationMother.createDownloadNotification().id
        }
    }

    @Test
    fun `clears all notifications`() {
        val store = StoreInstrumentation(NotificationsStateMachine, NotificationsState(NotificationMother.createDownloadNotification()))

        store.event(NotificationEvents.ClearAll)

        store.result.should { (states, effects) ->
            states.last().notifications.shouldBeEmpty()
            effects.shouldHaveSize(1).last().shouldBeInstanceOf<NotificationEffects.ClearAll>()
        }
    }
}
