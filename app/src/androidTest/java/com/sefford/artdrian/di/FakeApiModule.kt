package com.sefford.artdrian.di

import com.sefford.artdrian.WallpaperMother.WALLPAPER_LIST_DTO
import com.sefford.artdrian.data.dto.WallpaperResponse
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
    fun providesWallpaperApi(): WallpaperApi =
        FakeWallpaperApi { WallpaperResponse(WALLPAPER_LIST_DTO) }

    @Provides
    @Singleton
    fun providesCoroutineDispatcher() = Dispatchers.Unconfined
}
