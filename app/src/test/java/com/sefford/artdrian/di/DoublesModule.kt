package com.sefford.artdrian.di

import com.sefford.artdrian.common.FakeFileManager
import com.sefford.artdrian.common.FakeWallpaperAdapter
import com.sefford.artdrian.common.FileManager
import com.sefford.artdrian.common.WallpaperAdapter
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestScope
import javax.inject.Singleton

@Module
class DoublesModule(
    private val fileManagerResponse: () -> String = { "" },
    private val wallpaperManagerResponse: () -> Unit = { }
) {

    @Provides
    @Singleton
    fun provideFakeFileManager(): FileManager = FakeFileManager(fileManagerResponse)

    @Provides
    @Singleton
    fun provideFakeWallpaperAdapter(): WallpaperAdapter = FakeWallpaperAdapter(wallpaperManagerResponse)

    @Provides
    fun provideTestDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    @Singleton
    @Memory
    fun provideMemoryTestDispatcher(): CoroutineScope = TestScope()
}
