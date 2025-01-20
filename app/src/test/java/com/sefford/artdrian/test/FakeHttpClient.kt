package com.sefford.artdrian.test

import com.sefford.artdrian.common.data.network.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.head
import io.ktor.client.statement.HttpResponse
import io.ktor.client.HttpClient as KtorClient

class FakeHttpClient(
    private val client: KtorClient,
    private val behavior: (String) -> HttpResponse? = { null }
) : HttpClient {

    override suspend fun get(url: String, block: HttpRequestBuilder.() -> Unit): HttpResponse =
        behavior(url) ?: client.get(url, block)

    override suspend fun head(url: String, block: HttpRequestBuilder.() -> Unit): HttpResponse =
        behavior(url) ?: client.head(url, block)

    override fun close() {

    }
}
