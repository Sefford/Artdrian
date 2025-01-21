package com.sefford.artdrian.test.networking

import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.HttpResponseData
import io.ktor.http.Headers
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.core.toByteArray

fun MockRequestHandleScope.respondOnly(
    matcher: () -> Boolean = { true },
    content: String,
    status: HttpStatusCode = HttpStatusCode.OK,
    headers: Headers = headersOf()
): HttpResponseData =
    if (matcher()) {
        respond(ByteReadChannel(content.toByteArray(Charsets.UTF_8)), status, headers)
    } else {
        respond("", HttpStatusCode.NotFound)
    }
