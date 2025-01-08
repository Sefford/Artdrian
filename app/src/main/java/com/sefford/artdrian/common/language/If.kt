package com.sefford.artdrian.common.language

inline fun <T> Any?.orElse(block: () -> T) = this ?: block()
