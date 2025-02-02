package com.sefford.artdrian.common.di

import com.sefford.artdrian.common.utils.DefaultLogger
import com.sefford.artdrian.common.utils.Logger
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


    @Provides
    @Singleton
    fun provideLogger(logger: DefaultLogger): Logger = logger

}

