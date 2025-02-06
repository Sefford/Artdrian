package com.sefford.artdrian.common.di

import com.sefford.artdrian.common.utils.default
import com.sefford.artdrian.common.utils.io
import com.sefford.artdrian.common.utils.main
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import javax.inject.Singleton

@Module
class ConcurrencyModule {

    @Provides
    fun provideViewModelDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    @Memory
    fun provideMemoryCacheDispatcher(): CoroutineScope = MainScope().io(1)

    @Provides
    @Singleton
    @Disk
    fun provideMemoryDiskDispatcher(): CoroutineScope = MainScope().io(1)

    @Provides
    @Singleton
    @Default
    fun provideStoreDefaultScope(): CoroutineScope = MainScope().default()

    @Provides
    @Singleton
    @IO
    fun provideStoreIoScope(): CoroutineScope = MainScope().io()

    @Provides
    @Main
    fun provideMainScope(): CoroutineScope = MainScope().main()

}
