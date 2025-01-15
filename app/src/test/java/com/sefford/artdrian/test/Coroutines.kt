package com.sefford.artdrian.test

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.plus

fun CoroutineScope.unconfine() = this.plus(Dispatchers.Unconfined)
