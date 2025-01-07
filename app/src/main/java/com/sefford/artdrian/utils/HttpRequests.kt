package com.sefford.artdrian.utils

import com.sefford.artdrian.language.orFalse
import io.ktor.client.plugins.cache.storage.CachedResponseData
import io.ktor.http.HeadersBuilder
import io.ktor.http.HttpHeaders
import kotlinx.serialization.json.Json

fun HeadersBuilder.forceCache() {
    remove(HttpHeaders.CacheControl)
    remove(HttpHeaders.IfNoneMatch)
    append(HttpHeaders.CacheControl, FORCE_CACHE_HEADER)
}

val HeadersBuilder.isFromCache: Boolean
    get() = this[HttpHeaders.CacheControl]?.contains(FORCE_CACHE_HEADER).orFalse()

inline fun <reified T> CachedResponseData.body(deserializer: Json): T =
    deserializer.decodeFromString<T>(String(body))


private const val FORCE_CACHE_HEADER = "only-if-cached"

