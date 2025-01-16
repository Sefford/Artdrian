package com.sefford.artdrian.common.stores

import kotlinx.coroutines.flow.StateFlow

interface HoldsState<State> {
    val state: StateFlow<State>
    val current: State
}
