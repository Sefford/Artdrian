package com.sefford.artdrian.downloads.store

import com.sefford.artdrian.downloads.domain.model.Download
import com.sefford.artdrian.downloads.domain.model.Downloads

sealed class DownloadsEffects {

    data object LoadAll: DownloadsEffects()

    class Register(val downloads: Downloads) : DownloadsEffects() {
        constructor(download: Download): this(setOf(download))
    }

    class Update(val download: Download): DownloadsEffects(), External

    class Notify(val downloads: Downloads): DownloadsEffects(), External {
        constructor(download: Download): this(setOf(download))
    }

    data object Refresh: DownloadsEffects(), External

    sealed interface External
}
