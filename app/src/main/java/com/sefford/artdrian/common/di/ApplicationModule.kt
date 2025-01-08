package com.sefford.artdrian.common.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import com.sefford.artdrian.common.di.Application as ApplicationContext

@Module
class ApplicationModule(val application: Application) {

    @Provides
    fun provideApplication(): Application = application

    @Provides
    @ApplicationContext
    fun provideApplicationContext(): Context = application

}
