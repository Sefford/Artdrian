package com.sefford.artdrian.test

import com.sefford.artdrian.data.network.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.HttpClient as KtorClient

class FakeHttpClient(
    private val client: KtorClient,
    private val behavior: (String) -> HttpResponse? = { null }
): HttpClient {
    override suspend fun get(url: String): HttpResponse = behavior(url) ?: client.get(url)
}
