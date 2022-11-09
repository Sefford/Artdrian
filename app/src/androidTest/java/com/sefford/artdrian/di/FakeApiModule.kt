package com.sefford.artdrian.di

import com.sefford.artdrian.WallpaperMother.WALLPAPER_LIST
import com.sefford.artdrian.datasources.FakeWallpaperApi
import com.sefford.artdrian.datasources.WallpaperApi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FakeApiModule {

    @Provides
    @Singleton
    fun providesWallpaperApi(): WallpaperApi = FakeWallpaperApi{ WALLPAPER_LIST.map { it.metadata } }
}
