package com.sefford.artdrian.wallpapers.store

import com.sefford.artdrian.common.stores.KotlinStore
import com.sefford.artdrian.common.stores.Store
import kotlinx.coroutines.CoroutineScope

typealias WallpaperStore = KotlinStore<WallpaperEvents, WallpapersState, WallpaperEffects>
