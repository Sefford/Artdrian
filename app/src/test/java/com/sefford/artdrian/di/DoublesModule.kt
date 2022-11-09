package com.sefford.artdrian.di

import com.sefford.artdrian.common.FakeFileManager
import com.sefford.artdrian.common.FileManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DoublesModule(private val fileManagerResponse: () -> String = { "" }) {

    @Provides
    @Singleton
    fun provideFakeFileManager(): FileManager = FakeFileManager(fileManagerResponse)
}
