package com.sefford.artdrian.stores

import kotlinx.coroutines.flow.Flow

interface DispatchesEffects<Effect> {
    val effects: Flow<Effect>
}
