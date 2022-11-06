package com.sefford.artdrian.di

import com.sefford.artdrian.Artpplication
import com.sefford.artdrian.wallpaperlist.ui.WallpaperListViewModel
import dagger.Component
import javax.inject.Singleton

@Component(modules = [ApplicationModule::class])
@Singleton
interface ApplicationComponent {

    fun inject(application: Artpplication)

    fun inject(viewModel: WallpaperListViewModel)
}
