package com.sefford.artdrian.language

inline fun <T> Any?.orElse(block: () -> T) = this ?: block()
