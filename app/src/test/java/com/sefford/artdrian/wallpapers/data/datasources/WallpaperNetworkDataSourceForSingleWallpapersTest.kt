package com.sefford.artdrian.wallpapers.data.datasources

import com.sefford.artdrian.common.data.DataError
import com.sefford.artdrian.common.data.Endpoints
import com.sefford.artdrian.common.data.network.HttpCache
import com.sefford.artdrian.common.data.network.HttpClient
import com.sefford.artdrian.test.FakeHttpClient
import com.sefford.artdrian.test.InjectableTest
import com.sefford.artdrian.test.LazyMockEngineHandler
import com.sefford.artdrian.test.Resources
import com.sefford.artdrian.test.respondOnly
import com.sefford.artdrian.wallpapers.domain.model.Wallpaper
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.ktor.client.engine.mock.respondError
import io.ktor.client.plugins.cache.storage.CachedResponseData
import io.ktor.http.HttpProtocolVersion
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import io.ktor.http.headersOf
import io.ktor.util.date.GMTDate
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.inject.Inject
import io.ktor.client.HttpClient as KtorClient

class WallpaperNetworkDataSourceForSingleWallpapersTest : Resources, InjectableTest() {

    @Inject
    internal lateinit var client: HttpClient

    @Inject
    internal lateinit var ktor: KtorClient

    @Inject
    internal lateinit var handlers: LazyMockEngineHandler

    @Inject
    internal lateinit var cache: HttpCache

    @BeforeEach
    override fun beforeEach() {
        super.beforeEach()
        graph.inject(this)
    }

    @Test
    fun `returns the payload`() = runTest {
        handlers.queue { request ->
            respondOnly(
                { request.url.encodedPath.endsWith("1.json") },
                content = "single-wallpaper-response.json".asResponse(),
                headers = headersOf("Content-Type", "text/x-component")
            )
        }


        WallpaperNetworkDataSource(client, cache).getMetadata("1").first().should { response ->
            response.shouldBeRight()
            response.value.shouldBeInstanceOf<Wallpaper>()
        }
    }

    @Test
    fun `handles no connection`() = runTest {
        handlers.queue { _ -> throw CONNECTIVITY_ERROR }

        WallpaperNetworkDataSource(client, cache).getMetadata("1").first().should { response ->
            response.shouldBeLeft()
            response.value.shouldBeInstanceOf<DataError.Network.NoConnection>()
        }
    }

    @Test
    fun `proritizes cache`() = runTest {
        cache.enableSingleWallpaperCache()
        handlers.queue { _ ->
            throw CONNECTIVITY_ERROR
        }

        WallpaperNetworkDataSource(client, cache).getMetadata("1").first().should { response ->
            response.shouldBeRight()
            response.value.shouldBeInstanceOf<Wallpaper>()
        }
    }

    @Test
    fun `defaults to cache`() = runTest {
        cache.enableSingleWallpaperCache()
        val fakeClient: HttpClient = FakeHttpClient(ktor) { _ ->
            throw CONNECTIVITY_ERROR
        }
        WallpaperNetworkDataSource(fakeClient, cache).getMetadata("1").first().should { response ->
            response.shouldBeRight()
            response.value.shouldBeInstanceOf<Wallpaper>()
        }
    }

    @Test
    fun `fails returning the status code`() = runTest {
        handlers.queue { _ ->
            respondError(
                status = HttpStatusCode.BadRequest,
            )
        }

        WallpaperNetworkDataSource(client, cache).getMetadata("1").first().should { response ->
            response.shouldBeLeft()
            response.value.shouldBeInstanceOf<DataError.Network.Invalid>()
            (response.value as DataError.Network.Invalid).status shouldBe HttpStatusCode.BadRequest.value
        }
    }

    @Test
    fun `fails critically`() = runTest {
        handlers.queue { request ->
            respondOnly(
                { request.url.encodedPath.endsWith("1.json") },
                content = "{}",
                headers = headersOf("Content-Type", "text/x-component")
            )
        }
    }

    private suspend fun HttpCache.enableSingleWallpaperCache() {
        val url = Url("${Endpoints.ENDPOINT}wallpapers/1.json")
        cache.store(
            url, data =
            CachedResponseData(
                url = url,
                statusCode = HttpStatusCode.OK,
                requestTime = GMTDate(),
                responseTime = GMTDate(),
                version = HttpProtocolVersion.HTTP_2_0,
                expires = GMTDate(Long.MAX_VALUE),
                headers = headersOf("Content-Type", "text/x-component"),
                varyKeys = emptyMap(),
                body = "single-wallpaper-response.json".asResponse().toByteArray()
            )
        )
    }

    private val CONNECTIVITY_ERROR = UnresolvedAddressException()

}
