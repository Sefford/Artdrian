package com.sefford.artdrian.di

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
