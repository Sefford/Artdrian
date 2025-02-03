package com.sefford.artdrian.notifications.effects

import com.sefford.artdrian.notifications.model.Notifications
import com.sefford.artdrian.notifications.store.NotificationEffects
import com.sefford.artdrian.test.mothers.NotificationMother
import com.sefford.artdrian.test.unconfine
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainOnly
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class NotificationsEffectHandlerTest {

    private val notified = mutableListOf<Notifications>()
    private val cleared = mutableListOf<Int>()
    private var clearedAllNotifications = false
    private val clearedAll = { clearedAllNotifications = true }

    @Test
    fun `notifies the center`() = runTest {
        givenAnEffectHandler(backgroundScope.unconfine()).handle(
            NotificationEffects.Notify(NotificationMother.createDownloadNotification())
        )

        notified.shouldContainOnly(NotificationMother.createDownloadNotification())
        cleared.shouldBeEmpty()
        clearedAllNotifications.shouldBeFalse()
    }

    @Test
    fun `clears a notification`() = runTest {
        givenAnEffectHandler(backgroundScope.unconfine()).handle(NotificationEffects.Clear(NOTIFICATION_ID))

        notified.shouldBeEmpty()
        cleared.shouldContainOnly(NOTIFICATION_ID)
        clearedAllNotifications.shouldBeFalse()
    }

    @Test
    fun `clears all notification`() = runTest {
        givenAnEffectHandler(backgroundScope.unconfine()).handle(NotificationEffects.ClearAll)

        notified.shouldBeEmpty()
        cleared.shouldBeEmpty()
        clearedAllNotifications.shouldBeTrue()
    }

    private fun givenAnEffectHandler(scope: CoroutineScope) =
        NotificationsEffectHandler(notified::add, cleared::add, clearedAll, scope)
}

private const val NOTIFICATION_ID = 9284
