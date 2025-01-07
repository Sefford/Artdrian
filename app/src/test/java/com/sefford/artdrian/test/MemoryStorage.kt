package com.sefford.artdrian.test

import io.ktor.client.plugins.cache.storage.CacheStorage
import io.ktor.client.plugins.cache.storage.CachedResponseData
import io.ktor.http.Url

class MemoryStorage(private val cache: MutableMap<String, CachedResponseData> = mutableMapOf()) : CacheStorage {


    override suspend fun store(url: Url, data: CachedResponseData) {
        cache[url.toString()] = data
    }

    override suspend fun find(url: Url, varyKeys: Map<String, String>): CachedResponseData? {
        return cache[url.toString()]
    }

    override suspend fun findAll(url: Url): Set<CachedResponseData> = find(url, emptyMap())?.let { setOf(it) } ?: emptySet()

    fun clear() {
        cache.clear()
    }

}
