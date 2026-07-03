package com.prayatna.lookiesapp.module

import com.prayatna.lookiesapp.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.FlowType
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.realtime.realtime
import io.github.jan.supabase.serializer.KotlinXSerializer
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import io.ktor.client.engine.okhttp.OkHttpEngine
import io.ktor.client.engine.okhttp.OkHttpConfig
import okhttp3.ConnectionPool
import javax.inject.Singleton
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.seconds

@InstallIn(SingletonComponent::class)
@Module
object SupabaseModule {

    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient {
        return createSupabaseClient(
            supabaseKey = BuildConfig.API_KEY,
            supabaseUrl = BuildConfig.BASE_URL
        ) {
            requestTimeout = 30.seconds
            httpEngine = OkHttpEngine(OkHttpConfig().apply {
                config {
                    retryOnConnectionFailure(true)
                    connectTimeout(15, TimeUnit.SECONDS)
                    readTimeout(30, TimeUnit.SECONDS)
                    writeTimeout(30, TimeUnit.SECONDS)
                    // Shorter keep-alive prevents stale connections from causing
                    // "connect_timeout=unknown ms" errors after network changes
                    connectionPool(ConnectionPool(5, 60, TimeUnit.SECONDS))
                }
            })
            install(Postgrest) {
                serializer = KotlinXSerializer(
                    json = kotlinx.serialization.json.Json {
                        ignoreUnknownKeys = true
                        coerceInputValues = true
                        encodeDefaults = true
                        isLenient = true
                        explicitNulls = false
                    }
                )
            }
            install(Auth) {
                flowType = FlowType.PKCE
                scheme = "app"
                host = "supabase.com"
            }
            install(Storage)
            install(Realtime)
        }
    }

    @Provides
    @Singleton
    fun provideSupabaseDatabase(client: SupabaseClient): Postgrest {
        return client.postgrest
    }

    @Provides
    @Singleton
    fun provideSupabaseAuth(client: SupabaseClient): Auth {
        return client.auth
    }

    @Provides
    @Singleton
    fun provideSupabaseStorage(client: SupabaseClient): Storage {
        return client.storage
    }

    @Provides
    @Singleton
    fun provideSupabaseRealtime(client: SupabaseClient): Realtime {
        return client.realtime
    }
}