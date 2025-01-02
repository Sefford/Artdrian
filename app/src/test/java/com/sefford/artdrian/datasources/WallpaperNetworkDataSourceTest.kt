package com.sefford.artdrian.datasources

import com.sefford.artdrian.data.DataError
import com.sefford.artdrian.di.CoreModule
import com.sefford.artdrian.model.WallpaperList
import com.sefford.artdrian.test.Resources
import com.sefford.artdrian.test.respondOnly
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockEngineConfig
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

class WallpaperNetworkDataSourceTest : Resources {

    private val cacheDir: File = File("/http_cache")

    @Test
    fun `returns the payload`() {
        runTest {
            val client = CoreModule().provideHttpClient(cacheDir, mockEngine { request ->
                respondOnly(
                    { request.url.encodedPath.endsWith("index.json") },
                    content = "single-wallpaper-list-response.json".asResponse(),
                    headers = headersOf("Content-Type", "text/x-component")
                )
            })

            WallpaperNetworkDataSource(client).getMetadata().first().should { response ->
                response.shouldBeRight()
                response.value.shouldBeInstanceOf<WallpaperList>()
                (response.value as WallpaperList).wallpapers.shouldHaveSize(1)
            }
        }
    }

    @Test
    fun `fails returning the status code`() {
        runTest {
            val client = CoreModule().provideHttpClient(cacheDir, mockEngine { request ->
                respond(
                    content = "",
                    status = HttpStatusCode.ServiceUnavailable
                )
            })

            WallpaperNetworkDataSource(client).getMetadata().first().should { response ->
                response.shouldBeLeft()
                response.value.shouldBeInstanceOf<DataError.Network.Invalid>()
                (response.value as DataError.Network.Invalid).status shouldBe 503
            }
        }
    }

    @Test
    fun `fails critically`() {
        runTest {
            val client = CoreModule().provideHttpClient(cacheDir, mockEngine { request ->
                throw SOCKET_EXCEPTION
            })

            WallpaperNetworkDataSource(client).getMetadata().first().should { response ->
                response.shouldBeLeft()
                response.value.shouldBeInstanceOf<DataError.Network.Critical>()
            }
        }
    }

    private fun mockEngine(config: suspend MockRequestHandleScope.(HttpRequestData) -> HttpResponseData) =
        object : HttpClientEngineFactory<MockEngineConfig> {
            override fun create(block: MockEngineConfig.() -> Unit): HttpClientEngine {
                return MockEngine(config)
            }
        }

    private val SOCKET_EXCEPTION = SocketTimeoutException("Simulated socket timeout")
}
