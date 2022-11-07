package com.sefford.artdrian.di

import com.sefford.artdrian.wallpaperlist.ui.WallpaperListViewModel
import dagger.Component
import javax.inject.Singleton

@Component(modules = [CoreModule::class])
@Singleton
interface TestComponent {

    fun inject(viewModel: WallpaperListViewModel)

}
