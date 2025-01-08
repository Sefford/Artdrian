package com.sefford.artdrian.wallpapers.ui.detail.di

import dagger.Module
import dagger.Provides

@Module
class WallpaperDetailModule(private val wallpaperId: String) {

    @Provides
    @WallpaperId
    fun provideWallpaperId(): String = wallpaperId
}
