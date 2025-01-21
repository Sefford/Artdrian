package com.sefford.artdrian.common.data.network

import io.ktor.client.plugins.cache.storage.CacheStorage
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Url

suspend fun CacheStorage.find(url: String) = find(Url(url), emptyMap())
