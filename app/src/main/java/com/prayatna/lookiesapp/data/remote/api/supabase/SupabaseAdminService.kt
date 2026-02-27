package com.prayatna.lookiesapp.data.remote.api.supabase

import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.result.PostgrestResult
import javax.inject.Inject

class SupabaseAdminService @Inject constructor(
    private val auth: Auth,
    private val postgrest: Postgrest
) {
    suspend fun decidePartnerApplication(status: String, id: String): PostgrestResult {
         val result = postgrest.from("merchant_accounts").update(
            {
                set("kyc_status", status)
            }
        ) {
            filter {
                eq("business_id", id)
            }
        }
        return result
    }
}