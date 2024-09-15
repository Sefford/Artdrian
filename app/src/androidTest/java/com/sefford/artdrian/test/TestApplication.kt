package com.sefford.artdrian.test

import android.app.Application
import com.sefford.artdrian.data.Endpoints
import com.sefford.artdrian.TopComponentHolder
import com.sefford.artdrian.di.ApplicationComponent
import com.sefford.artdrian.di.ApplicationModule
import com.sefford.artdrian.di.CoreModule
import com.sefford.artdrian.di.DaggerTestApplicationComponent

class TestApplication: Application(), TopComponentHolder {

    override val graph: ApplicationComponent = DaggerTestApplicationComponent.builder()
        .applicationModule(ApplicationModule(this))
        .coreModule(CoreModule(Endpoints.ENDPOINT))
        .build()
}
