package com.sefford.artdrian.common.utils

import com.sefford.artdrian.common.language.orFalse
import io.ktor.client.plugins.cache.storage.CachedResponseData
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
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

val disableCache: HttpRequestBuilder.() -> Unit = {
    header(HttpHeaders.CacheControl, "no-cache, no-store, must-revalidate")
    header(HttpHeaders.Pragma, "no-cache")
    header(HttpHeaders.Expires, "0")
}


private const val FORCE_CACHE_HEADER = "only-if-cached"

