package com.prayatna.lookiesapp.data.remote.api.supabase

import android.util.Log
import com.auth0.android.jwt.JWT
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import javax.inject.Inject

class SupabaseAdminService @Inject constructor(
    private val auth: Auth,
    private val postgrest: Postgrest
) {
    private fun isAdmin(): Boolean {
        val accessToken = auth.currentAccessTokenOrNull() ?: return false
        val jwt = JWT(accessToken)
        val role = jwt.getClaim("app_metadata").asObject(Map::class.java)?.get("role")
        return role == "admin"
    }

    suspend fun decidePartnerApplication(status: String, id: String): String {
        if (!isAdmin()) {
            throw Exception("Only admin can approve partner application")
        }
        postgrest.from("partner_profiles").update(
            {
                set("status", status)
            }
        ) {
            filter {
                eq("id", id)
            }
        }
        return "Status updated to $status"
    }
}