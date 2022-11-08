package com.sefford.artdrian.wallpaperdetail.di

import dagger.Module
import dagger.Provides

@Module
class WallpaperDetailModule(private val wallpaperId: String) {

    @Provides
    @WallpaperId
    fun provideWallpaperId(): String = wallpaperId
}
