package com.sefford.artdrian.common.data.network

import com.sefford.artdrian.common.utils.body
import io.ktor.client.plugins.cache.storage.CacheStorage
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HttpCache @Inject constructor(val cache: CacheStorage, val deserializer: Json) {

    suspend inline fun <reified T> get(url: String): T? =
        cache.find(url)?.body<T>(deserializer)
}
