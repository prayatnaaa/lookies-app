package com.prayatna.lookiesapp.data.remote.api.supabase

import com.prayatna.lookiesapp.data.remote.dto.MerchantMemberDto
import com.prayatna.lookiesapp.data.remote.dto.MerchantProfileDto
import io.github.jan.supabase.postgrest.Postgrest
import javax.inject.Inject

class SupabaseMerchantService @Inject constructor(
    private val postgrest: Postgrest
) {

    suspend fun getMerchantProfile(id: String, merchantType: String? = null): MerchantProfileDto {
        val response = postgrest
            .from("merchant_account_profiles")
            .select {
                filter {
                    eq("business_id", id)
                    if (merchantType != null) eq("merchant_type", merchantType)
                }
            }.decodeSingle<MerchantProfileDto>()

        return response
    }

    suspend fun getMerchantMembers(userId: String? = null): List<MerchantMemberDto> {
        val response = postgrest
            .from("merchant_member_views")
            .select {
                filter {
                    if (userId != null) eq("user_id", userId)
                }
            }.decodeList<MerchantMemberDto>()
        return response
    }
}