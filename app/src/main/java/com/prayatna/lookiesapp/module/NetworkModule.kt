package com.prayatna.lookiesapp.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(OkHttp) {

            engine {
                config {
                    retryOnConnectionFailure(true)

                    connectTimeout(15, java.util.concurrent.TimeUnit.SECONDS)
                    readTimeout(15, java.util.concurrent.TimeUnit.SECONDS)
                    writeTimeout(15, java.util.concurrent.TimeUnit.SECONDS)
                }
            }

            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                        prettyPrint = true
                    }
                )
            }

            expectSuccess = false
        }
    }
}