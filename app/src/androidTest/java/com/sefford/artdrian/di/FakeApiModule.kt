package com.sefford.artdrian.di

import com.sefford.artdrian.WallpaperMother.WALLPAPER_LIST
import com.sefford.artdrian.data.dto.deserializers.WallpaperResponse
import com.sefford.artdrian.datasources.FakeWallpaperApi
import com.sefford.artdrian.datasources.WallpaperApi
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
class FakeApiModule {

    @Provides
    @Singleton
    fun providesWallpaperApi(): WallpaperApi = FakeWallpaperApi{ WallpaperResponse(WALLPAPER_LIST.map { it.metadata }) }

    @Provides
    @Singleton
    fun providesCoroutineDispatcher() = Dispatchers.Unconfined
}
