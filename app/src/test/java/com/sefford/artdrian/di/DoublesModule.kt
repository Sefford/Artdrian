package com.sefford.artdrian.di

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.sefford.artdrian.common.FakeFileManager
import com.sefford.artdrian.common.FakeWallpaperAdapter
import com.sefford.artdrian.common.FileManager
import com.sefford.artdrian.common.WallpaperAdapter
import com.sefford.artdrian.wallpapers.effects.WallpaperDomainEffectHandler
import com.sefford.artdrian.wallpapers.store.WallpaperStore
import com.sefford.artdrian.wallpapers.store.WallpapersState
import dagger.Module
import dagger.Provides
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
    fun provideWallpaperStore(): WallpaperStore = WallpaperStore(WallpapersState.Idle, TestScope())
}
