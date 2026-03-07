package com.prayatna.lookiesapp.data.remote.api.supabase

import com.prayatna.lookiesapp.data.remote.dto.MerchantProfileDto
import io.github.jan.supabase.postgrest.Postgrest
import javax.inject.Inject

class SupabaseMerchantService @Inject constructor(
    private val postgrest: Postgrest
) {

    suspend fun getMerchantProfile(id: String): MerchantProfileDto {
        val response = postgrest
            .from("merchant_account_profiles")
            .select {
                filter {
                    eq("business_id", id)
                }
            }.decodeSingle<MerchantProfileDto>()

        return response
    }
}