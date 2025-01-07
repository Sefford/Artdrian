package com.sefford.artdrian.data.network

import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.HttpClient as KtorClient

class DelegatedHttpClient(private val client: KtorClient) : HttpClient {
    override suspend fun get(url: String): HttpResponse = client.get(url)
}
