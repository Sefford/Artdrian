package com.sefford.artdrian.common.stores

import kotlinx.coroutines.flow.Flow

interface DispatchesEffects<Effect> {
    val effects: Flow<Effect>
}
