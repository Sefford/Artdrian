package com.sefford.artdrian.common.language

inline fun <T> T?.orElse(block: () -> T) = this ?: block()

inline fun <reified T> Any?.takeIfIs() = takeIf { this is T } as? T

inline fun <reified T, R> Any?.takeIfIs(block: (T) -> R): R? = takeIfIs<T>()?.let(block)

