package com.sefford.artdrian.di

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
class FakeApiModule {

    @Provides
    @Singleton
    fun providesCoroutineDispatcher() = Dispatchers.Unconfined
}
