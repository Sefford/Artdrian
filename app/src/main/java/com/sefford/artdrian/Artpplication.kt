package com.sefford.artdrian

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.request.crossfade
import coil3.util.DebugLogger
import com.sefford.artdrian.di.ApplicationModule
import com.sefford.artdrian.di.CoreModule
import com.sefford.artdrian.di.DaggerApplicationComponent
import com.sefford.artdrian.utils.debuggable
import com.sefford.artdrian.wallpapers.store.WallpaperEvents
import com.sefford.artdrian.wallpapers.store.WallpaperStore
import javax.inject.Inject

class Artpplication : Application(), TopComponentHolder, SingletonImageLoader.Factory {

    @Inject
    internal lateinit var store: WallpaperStore

    override val graph = DaggerApplicationComponent.builder()
        .applicationModule(ApplicationModule(this))
        .coreModule(CoreModule())
        .build()

    override fun onCreate() {
        super.onCreate()
        graph.inject(this)
        store.event(WallpaperEvents.Load)
    }

    override fun newImageLoader(context: PlatformContext): ImageLoader =
        ImageLoader.Builder(context)
        .crossfade(true)
        .apply {
            if (context.applicationInfo.debuggable) {
                logger(DebugLogger())
            }
        }
        .build()
}
