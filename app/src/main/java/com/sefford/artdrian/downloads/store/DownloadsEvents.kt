package com.sefford.artdrian.downloads.store

import com.sefford.artdrian.common.data.DataError
import com.sefford.artdrian.downloads.domain.model.Download
import com.sefford.artdrian.downloads.domain.model.Downloads

sealed class DownloadsEvents {

    data object LoadAll: DownloadsEvents()

    class Register(val downloads: Set<Download.Pending>): DownloadsEvents()

    class OnDownloadsReceived(val downloads: Downloads): DownloadsEvents()

    class OnErrorReceived(val error: DataError) : DownloadsEvents()

    class Update(val download: Download): DownloadsEvents()

    data object Refresh: DownloadsEvents()
}
