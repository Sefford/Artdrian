package com.sefford.artdrian.common.data.network

import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.head
import io.ktor.client.statement.HttpResponse
import io.ktor.client.HttpClient as KtorClient

class DelegatedHttpClient(private val client: KtorClient) : HttpClient {

    override suspend fun get(url: String, block: HttpRequestBuilder.() -> Unit): HttpResponse = client.get(url, block)

    override suspend fun head(url: String, block: HttpRequestBuilder.() -> Unit): HttpResponse = client.head(url, block)

    override fun close() {
        client.close()
    }
}
