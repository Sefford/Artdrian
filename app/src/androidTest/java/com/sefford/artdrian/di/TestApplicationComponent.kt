package com.sefford.artdrian.di

import com.sefford.artdrian.common.di.AndroidModule
import com.sefford.artdrian.common.di.ApplicationComponent
import com.sefford.artdrian.common.di.ApplicationModule
import com.sefford.artdrian.common.di.CoreModule
import dagger.Component
import javax.inject.Singleton

@Component(modules = [
    ApplicationModule::class,
    CoreModule::class,
    AndroidModule::class,
    FakeApiModule::class
])
@Singleton
interface TestApplicationComponent: ApplicationComponent
