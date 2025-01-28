package com.sefford.artdrian.notifications.model

import com.sefford.artdrian.common.language.percentage

sealed class Notifications {
    class DownloadNotification(
        val downloads: Int,
        val total: Long,
        val progress: Long,
    ) : Notifications() {
        val percentage: Float = progress.percentage(total)
    }
}
