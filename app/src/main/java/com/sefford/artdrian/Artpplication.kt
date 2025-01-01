package com.sefford.artdrian

import android.app.Application
import com.sefford.artdrian.di.ApplicationModule
import com.sefford.artdrian.di.CoreModule
import com.sefford.artdrian.di.DaggerApplicationComponent
import com.sefford.artdrian.wallpapers.store.WallpaperEvents
import com.sefford.artdrian.wallpapers.store.WallpaperStore
import javax.inject.Inject

class Artpplication : Application(), TopComponentHolder {

    @Inject
    protected lateinit var store: WallpaperStore

    override val graph = DaggerApplicationComponent.builder()
        .applicationModule(ApplicationModule(this))
        .coreModule(CoreModule())
        .build()

    override fun onCreate() {
        super.onCreate()
        graph.inject(this)
        store.event(WallpaperEvents.Load)
    }
}
