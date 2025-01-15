package com.sefford.artdrian.downloads.store

import com.sefford.artdrian.common.stores.KotlinStore

typealias DownloadsStore = KotlinStore<DownloadsEvents, DownloadsState, DownloadsEffects>
