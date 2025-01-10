package com.sefford.artdrian.di

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.sefford.artdrian.common.FakeFileManager
import com.sefford.artdrian.common.FakeWallpaperAdapter
import com.sefford.artdrian.common.FileManager
import com.sefford.artdrian.common.WallpaperAdapter
import com.sefford.artdrian.wallpapers.data.db.WallpaperDatabase
import com.sefford.artdrian.common.di.Application
import com.sefford.artdrian.common.di.Memory
import com.sefford.artdrian.common.di.NetworkCache
import com.sefford.artdrian.test.FakeLogger
import com.sefford.artdrian.test.MemoryStorage
import com.sefford.artdrian.test.LazyMockEngineHandler
import com.sefford.artdrian.test.MockEngineFactory
import com.sefford.artdrian.common.utils.Logger
import com.sefford.artdrian.wallpapers.effects.WallpaperDomainEffectHandler
import com.sefford.artdrian.wallpapers.store.WallpaperStateMachine
import com.sefford.artdrian.wallpapers.store.WallpaperStore
import com.sefford.artdrian.wallpapers.store.WallpapersState
import dagger.Module
import dagger.Provides
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.cache.storage.CacheStorage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestScope
import javax.inject.Singleton

@Module
class DoublesModule(
    private val fileManagerResponse: () -> String = { "" },
    private val wallpaperManagerResponse: () -> Unit = { }
) {

    @Provides
    @Singleton
    @Application
    fun provideContext() = ApplicationProvider.getApplicationContext<Context>()

    @Provides
    @Singleton
    @NetworkCache
    fun provideNetworkCache(@Application context: Context) = context.externalCacheDir!!

    @Provides
    @Singleton
    fun provideFakeFileManager(): FileManager = FakeFileManager(fileManagerResponse)

    @Provides
    @Singleton
    fun provideFakeWallpaperAdapter(): WallpaperAdapter = FakeWallpaperAdapter(wallpaperManagerResponse)

    @Provides
    @Singleton
    fun provideTestDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    @Singleton
    @Memory
    fun provideMemoryTestDispatcher(): CoroutineScope = TestScope()

    @Provides
    @Singleton
    @Memory
    fun provideDomainEffect(): WallpaperDomainEffectHandler = WallpaperDomainEffectHandler({ flow { } }, { flow { } })

    @Provides
    @Singleton
    @Memory
    fun provideWallpaperStore(): WallpaperStore = WallpaperStore(WallpaperStateMachine, WallpapersState.Idle.Empty, TestScope())

    @Provides
    @Singleton
    fun providesLogger(): Logger = FakeLogger()

    @Provides
    @Singleton
    fun providesDatabase(@Application context: Context): WallpaperDatabase =
        Room.inMemoryDatabaseBuilder(context, WallpaperDatabase::class.java).allowMainThreadQueries().build()

    @Provides
    @Singleton
    fun provideMemoryHttpStorage() = MemoryStorage()

    @Provides
    @Singleton
    fun provideHttpStorage(cache: MemoryStorage): CacheStorage = cache

    @Provides
    @Singleton
    fun provideMockEngineLazyConfiguration(): LazyMockEngineHandler = LazyMockEngineHandler()

    @Provides
    @Singleton
    fun provideEngine(handlers: LazyMockEngineHandler): HttpClientEngineFactory<*> = MockEngineFactory(handlers)

}
