package com.sefford.artdrian.test

import android.app.Application
import com.sefford.artdrian.common.di.TopComponentHolder
import com.sefford.artdrian.common.di.ApplicationComponent
import com.sefford.artdrian.common.di.ApplicationModule
import com.sefford.artdrian.common.di.CoreModule
import com.sefford.artdrian.di.DaggerTestApplicationComponent

class TestApplication: Application(), TopComponentHolder {

    override val graph: ApplicationComponent = DaggerTestApplicationComponent.builder()
        .applicationModule(ApplicationModule(this))
        .coreModule(CoreModule())
        .build()
}
