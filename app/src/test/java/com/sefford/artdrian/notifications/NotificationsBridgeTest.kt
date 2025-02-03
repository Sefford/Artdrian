package com.sefford.artdrian.notifications

import com.sefford.artdrian.common.language.files.Size.Companion.bytes
import com.sefford.artdrian.downloads.store.DownloadsEffects
import com.sefford.artdrian.notifications.model.Notifications
import com.sefford.artdrian.test.mothers.DownloadsMother
import com.sefford.artdrian.test.unconfine
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class NotificationsBridgeTest {

    private val notifications = mutableListOf<Notifications.DownloadNotification>()
    private var cleared = false
    private val clear = { cleared = true }
    private val effects = MutableSharedFlow<DownloadsEffects>()

    @Test
    fun `initializes the information the first time`() = runTest {
        effects.bridgeNotifications(
            canNotify = { true },
            notify = notifications::add,
            clear = clear,
            scope = backgroundScope.unconfine()
        )

        effects.emit(DownloadsEffects.Notify(DownloadsMother.createPending()))

        notifications.last().should { notification ->
            notification.number shouldBe 1
            notification.total shouldBe 0.bytes
            notification.progress shouldBe 0.bytes
            notification.percentage shouldBe 0
            notification.indeterminate.shouldBeTrue()
        }
        cleared.shouldBeFalse()
    }

    @Test
    fun `does not execute if the notifications are disabled`() = runTest {
        effects.bridgeNotifications(
            canNotify = { false },
            notify = notifications::add,
            clear = clear,
            scope = backgroundScope.unconfine()
        )

        effects.emit(DownloadsEffects.Notify(DownloadsMother.createPending()))

        notifications.shouldBeEmpty()
        cleared.shouldBeFalse()
    }

    @Test
    fun `updates a download`() = runTest {
        effects.bridgeNotifications(
            canNotify = { true },
            notify = notifications::add,
            clear = clear,
            scope = backgroundScope.unconfine()
        )

        effects.emit(DownloadsEffects.Notify(DownloadsMother.createPending()))
        effects.emit(DownloadsEffects.Update(DownloadsMother.createOngoing()))

        notifications.last().should { notification ->
            notification.number shouldBe 1
            notification.total shouldBe TOTAL
            notification.progress shouldBe 0.bytes
            notification.percentage shouldBe 0
            notification.indeterminate.shouldBeFalse()
        }
        cleared.shouldBeFalse()
    }

    @Test
    fun `clears notification when all the downloads are finished`() = runTest {
        effects.bridgeNotifications(
            canNotify = { true },
            notify = notifications::add,
            clear = clear,
            scope = backgroundScope.unconfine()
        )

        effects.emit(DownloadsEffects.Notify(DownloadsMother.createPending()))
        effects.emit(DownloadsEffects.Update(DownloadsMother.createFinished()))

        cleared.shouldBeTrue()
    }

    @Test
    fun `reinits the cycle`() = runTest {
        effects.bridgeNotifications(
            canNotify = { true },
            notify = notifications::add,
            clear = clear,
            scope = backgroundScope.unconfine()
        )

        effects.emit(DownloadsEffects.Notify(DownloadsMother.createPending()))
        effects.emit(DownloadsEffects.Update(DownloadsMother.createFinished()))
        effects.emit(DownloadsEffects.Update(DownloadsMother.createPending()))

        notifications.last().should { notification ->
            notification.number shouldBe 1
            notification.total shouldBe 0.bytes
            notification.progress shouldBe 0.bytes
            notification.percentage shouldBe 0
            notification.indeterminate.shouldBeTrue()
        }
    }
}

private val TOTAL = 1000.bytes
