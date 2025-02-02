package com.sefford.artdrian.downloads.store

import com.sefford.artdrian.downloads.domain.model.Download
import com.sefford.artdrian.downloads.domain.model.Downloads

sealed class DownloadsEffects {

    data object LoadAll: DownloadsEffects()

    class Register(val downloads: List<Download>) : DownloadsEffects()

    class Update(val download: Download): DownloadsEffects()

    class Notify(val downloads: Downloads): DownloadsEffects()

}
