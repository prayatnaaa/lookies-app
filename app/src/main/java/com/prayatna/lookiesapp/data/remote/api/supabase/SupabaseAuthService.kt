package com.prayatna.lookiesapp.data.remote.api.supabase

import android.util.Log
import com.auth0.android.jwt.JWT
import com.prayatna.lookiesapp.BuildConfig
import com.prayatna.lookiesapp.data.remote.dto.request.auth.RegisterRequest
import com.prayatna.lookiesapp.data.remote.dto.response.auth.LoginResponse
import com.prayatna.lookiesapp.data.remote.dto.response.auth.RegisterResponse
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SupabaseAuthService @Inject constructor(
    private val auth: Auth,
    private val httpClient: HttpClient
) {

    fun listenUserSession() = auth.sessionStatus

    suspend fun signIn(email: String, password: String): LoginResponse {
        auth.signInWith(Email) {
            this.email = email
            this.password = password
        }
        val accessToken = auth.currentAccessTokenOrNull()

        if (accessToken.isNullOrEmpty()) {
            return LoginResponse(success = false, role = "unknown")
        }

        val jwt = JWT(accessToken)
        val role = jwt.getClaim("app_metadata").asObject(Map::class.java)?.get("role")
            ?: "unknown"

        if (role == "unknown") {
            return LoginResponse(success = false, role = "unknown", message = "Role not found")
        }

        return LoginResponse(success = true, role = role.toString())
    }

    suspend fun signUp(request: RegisterRequest): RegisterResponse {
        val response = httpClient.post("${BuildConfig.SUPABASE_EDGE_BASE_URL}/register") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }

        return response.body()
    }

    fun getRole(): String {
        val accessToken = auth.currentAccessTokenOrNull()
        if (accessToken.isNullOrEmpty()) {
            throw Exception("unknown")
        }
        val jwt = JWT(accessToken)
        val role = jwt.getClaim("app_metadata").asObject(Map::class.java)?.get("role")
            ?: "unknown"

        if (role == "unknown") {
            throw Exception("Role not found")
        }
        Log.d("ROLE", role.toString())
        return role.toString()
    }
}