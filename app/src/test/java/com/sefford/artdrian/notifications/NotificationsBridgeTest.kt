package com.sefford.artdrian.notifications

import com.sefford.artdrian.common.language.files.writeString
import com.sefford.artdrian.downloads.store.DownloadsState
import com.sefford.artdrian.notifications.model.Notifications
import com.sefford.artdrian.test.mothers.DownloadsMother
import com.sefford.artdrian.test.unconfine
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import java.io.File

class NotificationsBridgeTest {

    private val events = mutableListOf<Notifications.DownloadNotification>()

    @Test
    fun `initializes the information the first time`() = runTest {
        MutableStateFlow<DownloadsState>((DownloadsState.Loaded(listOf(DownloadsMother.createPending()))))
            .bridgeNotifications(events::add, backgroundScope.unconfine())

        events.last().should { notification ->
            notification.downloads shouldBe 1
            notification.total shouldBe 0
            notification.progress shouldBe 0
            notification.percentage shouldBe 0
        }
    }

    @Test
    fun `filters out initially finished downloads`() = runTest {
        MutableStateFlow<DownloadsState>((DownloadsState.Loaded(listOf(DownloadsMother.createFinished()))))
            .bridgeNotifications(events::add, backgroundScope.unconfine())

        events.shouldBeEmpty()
    }

    @Test
    fun `keeps a download when it is finished`() = runTest {
        val downloadFile = File.createTempFile(DownloadsMother.createFinished().name, ".jpg").also { file ->
            file.writeString("a".repeat(DownloadsMother.createFinished().total.inBytes.toInt()))
        }

        val downloads = MutableStateFlow<DownloadsState>((DownloadsState.Loaded(listOf(DownloadsMother.createPending()))))

        downloads.bridgeNotifications(events::add, backgroundScope.unconfine())

        downloads.emit(DownloadsState.Loaded(listOf(DownloadsMother.createFinished(file = downloadFile))))

        events.last().should { notification ->
            notification.downloads shouldBe 1
            notification.total shouldBe 1000L
            notification.progress shouldBe 1000L
            notification.percentage shouldBe 100f
        }
    }
}
