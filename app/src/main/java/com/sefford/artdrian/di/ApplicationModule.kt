package com.sefford.artdrian.di

import android.app.Application
import android.content.Context
import com.sefford.artdrian.Artpplication
import dagger.Module
import dagger.Provides
import com.sefford.artdrian.di.Application as ApplicationContext

@Module
class ApplicationModule(val application: Artpplication) {

    @Provides
    fun provideApplication(): Application = application

    @Provides
    fun provideArtpplication(): Artpplication = application

    @Provides
    @ApplicationContext
    fun provideApplicationContext(): Context = application

}
