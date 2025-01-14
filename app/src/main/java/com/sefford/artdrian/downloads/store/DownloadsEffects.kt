package com.sefford.artdrian.downloads.store

import com.sefford.artdrian.downloads.domain.model.Downloads

sealed class DownloadsEffects {

    data object LoadAll: DownloadsEffects()

    class Preload(val downloads: Downloads): DownloadsEffects()
}
