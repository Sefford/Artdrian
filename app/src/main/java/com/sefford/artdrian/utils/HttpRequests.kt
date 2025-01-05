package com.sefford.artdrian.utils

import com.sefford.artdrian.language.orFalse
import io.ktor.http.HeadersBuilder
import io.ktor.http.HttpHeaders

fun HeadersBuilder.forceCache() {
    remove(HttpHeaders.CacheControl)
    remove(HttpHeaders.IfNoneMatch)
    append(HttpHeaders.CacheControl, FORCE_CACHE_HEADER)
}

val HeadersBuilder.isFromCache: Boolean
    get() = this[HttpHeaders.CacheControl]?.contains(FORCE_CACHE_HEADER).orFalse()

private const val FORCE_CACHE_HEADER = "only-if-cached"

