package com.sefford.artdrian.common.di

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.plus
import javax.inject.Singleton

@Module
class ConcurrencyModule {

    @Provides
    fun provideViewModelDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @OptIn(ExperimentalCoroutinesApi::class)
    @Provides
    @Singleton
    @Memory
    fun provideMemoryCacheDispatcher(): CoroutineScope = MainScope().plus(Dispatchers.IO.limitedParallelism(1))

    @OptIn(ExperimentalCoroutinesApi::class)
    @Provides
    @Singleton
    @Disk
    fun provideMemoryDiskDispatcher(): CoroutineScope = MainScope().plus(Dispatchers.IO.limitedParallelism(1))

}
