package com.sefford.artdrian.di

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
class ConcurrencyModule {

    @Provides
    fun provideViewModelDispatcher(): CoroutineDispatcher = Dispatchers.IO
}
