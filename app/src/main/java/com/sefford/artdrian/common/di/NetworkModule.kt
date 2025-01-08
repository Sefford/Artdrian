package com.sefford.artdrian.common.di

import dagger.Module
import dagger.Provides
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.cache.storage.CacheStorage
import io.ktor.client.plugins.cache.storage.FileStorage
import java.io.File
import javax.inject.Singleton

@Module
class NetworkModule {
    @Provides
    @Singleton
    fun provideEngine(): HttpClientEngineFactory<*> = CIO

    @Provides
    @Singleton
    fun provideHttpStorage(
        @NetworkCache httpCacheDir: File,
    ): CacheStorage = FileStorage(httpCacheDir)
}
