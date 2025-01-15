package com.sefford.artdrian.downloads.store

import com.sefford.artdrian.downloads.domain.model.Download

sealed class DownloadsEffects {

    data object LoadAll: DownloadsEffects()

    class Register(val downloads: List<Download>) : DownloadsEffects()
}
