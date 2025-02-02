package com.sefford.artdrian.common.di

import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
class CoreModule {
    @Provides
    @Singleton
    fun provideDeserialization(): Json = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
    }
}

