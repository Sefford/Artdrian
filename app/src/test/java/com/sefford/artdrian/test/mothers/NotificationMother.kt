package com.sefford.artdrian.test.mothers

import com.sefford.artdrian.downloads.domain.model.Downloads
import com.sefford.artdrian.notifications.model.Notifications

object NotificationMother {

    fun createDownloadNotification(
        downloads: Downloads = setOf(DownloadsMother.createOngoing())
    ) = Notifications.DownloadNotification(downloads)
}
