package com.prayatna.lookiesapp.data.remote.api.supabase

import android.util.Log
import com.auth0.android.jwt.JWT
import com.prayatna.lookiesapp.data.remote.response.auth.LoginResponse
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import javax.inject.Inject

class SupabaseAuthApi @Inject constructor(
    private val auth: Auth,
) {

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

        Log.d("ROLE", role.toString())

        return LoginResponse(success = true, role = role.toString())
    }
}