package com.sefford.artdrian.common.di

import com.sefford.artdrian.common.data.network.DelegatedHttpClient
import com.sefford.artdrian.common.utils.DefaultLogger
import com.sefford.artdrian.common.utils.Logger
import dagger.Module
import dagger.Provides
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.cache.storage.CacheStorage
import io.ktor.client.plugins.cache.storage.FileStorage
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import java.io.File
import javax.inject.Singleton
import com.sefford.artdrian.common.data.network.HttpClient as DelegatedClient

@Module
class NetworkModule {
    @Provides
    @Singleton
    fun provideEngine(): HttpClientEngineFactory<*> = CIO

    @Provides
    @Singleton
    fun provideHttpStorage(
        @NetworkCache httpCacheDir: File,
    ): CacheStorage = FileStorage(httpCacheDir)

    @Provides
    @Singleton
    fun provideHttpClient(
        deserialization: Json,
        engine: HttpClientEngineFactory<*> = CIO,
        storage: CacheStorage,
        logger: Logger = DefaultLogger()
    ): HttpClient {
        return HttpClient(engine) {
            install(ContentNegotiation) {
                json(json = deserialization, contentType = ContentType.parse("text/x-component"))
            }

            install(Logging) {
                this.logger = logger
                level = LogLevel.ALL
            }

            install(ResponseObserver) {
                onResponse { response ->
                    logger.log("TAG_HTTP_STATUS_LOGGER", "${response.status.value}")
                }
            }

            install(DefaultRequest) {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
            }

            install(HttpCache) {
                publicStorage(storage)
            }
        }
    }

    @Provides
    @Singleton
    fun provideDelegatedClient(client: HttpClient): DelegatedClient = DelegatedHttpClient(client)
}
