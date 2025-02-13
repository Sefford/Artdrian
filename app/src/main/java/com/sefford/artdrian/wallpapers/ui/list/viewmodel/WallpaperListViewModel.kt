package com.sefford.artdrian.wallpapers.ui.list.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sefford.artdrian.common.di.Default
import com.sefford.artdrian.common.di.Main
import com.sefford.artdrian.common.stores.DispatchesEffects
import com.sefford.artdrian.common.stores.HoldsState
import com.sefford.artdrian.common.stores.StoreEffectDispatching
import com.sefford.artdrian.wallpapers.store.WallpaperStore
import com.sefford.artdrian.wallpapers.ui.list.effects.WallpaperListEffect
import com.sefford.artdrian.wallpapers.ui.list.effects.WallpaperListNavigationEffectHandler
import com.sefford.artdrian.wallpapers.ui.list.effects.bridgeEffectHandler
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class WallpaperListViewModel @AssistedInject constructor(
    wallpapers: WallpaperStore,
    @Assisted initial: WallpaperListState,
    scope: CoroutineScope,
) : ViewModel(), HoldsState<WallpaperListState>, DispatchesEffects<WallpaperListEffect> by StoreEffectDispatching(scope) {

    override val state: StateFlow<WallpaperListState> = wallpapers.state.map { WallpaperListState(it, ::effect) }
        .stateIn(viewModelScope, SharingStarted.Lazily, initial)

    override val current: WallpaperListState
        get() = state.value

    class Provider @Inject constructor(
        private val wallpaperStore: WallpaperStore,
        private val handler: WallpaperListNavigationEffectHandler,
        @Default private val defaultSCope: CoroutineScope,
        @Main private val mainScope: CoroutineScope
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WallpaperListViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return WallpaperListViewModel(wallpaperStore, WallpaperListState.Loading, defaultSCope).also { vm ->
                    vm.bridgeEffectHandler(handler::handle, mainScope)
                } as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
