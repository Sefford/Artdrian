package com.sefford.artdrian.common.data.network

import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.statement.HttpResponse
import java.io.Closeable

interface HttpClient : Closeable {

    suspend fun get(url: String, block: HttpRequestBuilder.() -> Unit = {}): HttpResponse

    suspend fun head(url: String, block: HttpRequestBuilder.() -> Unit = {}): HttpResponse
}
