package com.sefford.artdrian.downloads.store

import com.sefford.artdrian.downloads.domain.model.Download
import com.sefford.artdrian.downloads.domain.model.Downloads

sealed class DownloadsEvents {

    class Preloads(val downloads: Downloads): DownloadsEvents()

    class OnDownloadsReceived(val downloads: Downloads): DownloadsEvents()
}
