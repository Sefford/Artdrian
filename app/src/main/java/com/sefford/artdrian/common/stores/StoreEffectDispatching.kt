package com.sefford.artdrian.common.stores

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class StoreEffectDispatching<Effect>(private val scope: CoroutineScope): DispatchesEffects<Effect> {
    private val _effects = MutableSharedFlow<Effect>()
    override val effects: Flow<Effect> = _effects.asSharedFlow()

    override fun effect(effect: Effect) {
        scope.launch { _effects.emit(effect) }
    }
}
