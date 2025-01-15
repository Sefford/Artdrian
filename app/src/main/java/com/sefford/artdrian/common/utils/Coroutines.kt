package com.sefford.artdrian.common.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.plus

fun CoroutineScope.io(parallelism: Int = Int.MAX_VALUE) = this.plus(Dispatchers.IO.limit(parallelism))

fun CoroutineScope.default(parallelism: Int = Int.MAX_VALUE) = this.plus(Dispatchers.Default.limit(parallelism))

private fun CoroutineDispatcher.limit(parallelism: Int) =
    if (parallelism < Int.MAX_VALUE) this.limitedParallelism(parallelism) else this
