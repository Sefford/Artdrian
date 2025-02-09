package com.sefford.artdrian.wallpapers.ui.detail.di

import androidx.navigation.NavHostController
import com.sefford.artdrian.common.di.Id
import dagger.Module
import dagger.Provides

@Module
class WallpaperDetailModule(
    private val id: String,
) {

    @Provides
    @Id
    fun provideWallpaperId() = id
}
