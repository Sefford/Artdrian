package com.sefford.artdrian.wallpapers.store

import com.sefford.artdrian.stores.KotlinStore
import com.sefford.artdrian.stores.Store
import kotlinx.coroutines.CoroutineScope

typealias WallpaperStore = KotlinStore<WallpaperEvents, WallpapersState, WallpaperEffects>
