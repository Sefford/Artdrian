package com.sefford.artdrian.common.language

inline fun <T> T?.orElse(block: () -> T) = this ?: block()
