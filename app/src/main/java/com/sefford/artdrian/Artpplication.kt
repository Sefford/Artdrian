package com.sefford.artdrian

import android.app.Application
import com.sefford.artdrian.di.ApplicationModule
import com.sefford.artdrian.di.DaggerApplicationComponent

class Artpplication : Application() {

    val graph = DaggerApplicationComponent.builder()
        .applicationModule(ApplicationModule(this))
        .build()

    override fun onCreate() {
        super.onCreate()
        graph.inject(this)
    }
}
