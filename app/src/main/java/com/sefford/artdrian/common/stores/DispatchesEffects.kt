package com.sefford.artdrian.common.stores

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow

interface DispatchesEffects<Effect> {
    val effects: Flow<Effect>

    fun effect(effect: Effect)
}
