package com.sefford.artdrian.common.data.network

import io.ktor.client.statement.HttpResponse

interface HttpClient {

    suspend fun get(url: String): HttpResponse

    suspend fun head(url: String): HttpResponse
}
