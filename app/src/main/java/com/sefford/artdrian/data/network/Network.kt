package com.sefford.artdrian.data.network

import io.ktor.client.plugins.cache.storage.CacheStorage
import io.ktor.http.Url

suspend fun CacheStorage.find(url: String) = find(Url(url), emptyMap())
