package com.prayatna.lookiesapp.data.remote.api.supabase

import com.prayatna.lookiesapp.data.remote.dto.response.admin.DecideEventResponseDto
import com.prayatna.lookiesapp.data.remote.dto.response.admin.DecidePartnerApplicationResponseDto
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Columns
import javax.inject.Inject

class SupabaseAdminService @Inject constructor(
    private val postgrest: Postgrest
) {
    suspend fun decidePartnerApplication(status: String, id: String): DecidePartnerApplicationResponseDto {
         val result = postgrest.from("merchant_accounts").update(
            {
                set("kyc_status", status)
            }
        ) {
             select(Columns.list("id", "business_id", "kyc_status"))
            filter {
                eq("business_id", id)
            }
        }.decodeSingle<DecidePartnerApplicationResponseDto>()
        return result
    }

    suspend fun decideEvent(status: String, id: Int): DecideEventResponseDto {
        val result = postgrest.from("events").update(
            {
                set("status", status)
            }
        ) {
            select(Columns.list("id", "organizer_id", "title", "status"))
            filter {
                eq("id", id)
            }
        }.decodeSingle<DecideEventResponseDto>()

        return result
    }
}