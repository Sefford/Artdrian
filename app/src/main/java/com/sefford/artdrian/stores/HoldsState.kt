package com.sefford.artdrian.stores

import kotlinx.coroutines.flow.StateFlow

interface HoldsState<State> {
    val state: StateFlow<State>
}
