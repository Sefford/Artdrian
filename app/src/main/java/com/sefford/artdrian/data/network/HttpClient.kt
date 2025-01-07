package com.sefford.artdrian.data.network

import io.ktor.client.statement.HttpResponse

fun interface HttpClient {

    suspend fun get(url: String): HttpResponse
}
