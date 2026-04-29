package com.prayatna.lookiesapp.data.remote.api.supabase

import com.prayatna.lookiesapp.data.remote.dto.GetKycDocumentDto
import com.prayatna.lookiesapp.data.remote.dto.TicketDto
import com.prayatna.lookiesapp.data.remote.dto.response.admin.DecideEventResponseDto
import com.prayatna.lookiesapp.data.remote.dto.response.admin.DecidePartnerApplicationResponseDto
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Columns
import javax.inject.Inject

class SupabaseAdminService @Inject constructor(
    private val postgrest: Postgrest,
    private val auth: Auth
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

    suspend fun rejectEvent(id: Int, rejectReason: String): DecideEventResponseDto {
        val userId = auth.currentSessionOrNull()?.user?.id ?: throw Exception("User not authenticated")
        val result = postgrest.from("events").update(
            mapOf(
                "status" to "rejected",
                "updated_at" to "now()",
                "approved_by" to userId,
                "approved_at" to "now()",
                "rejection_reason" to rejectReason
            )
        ) {
            select(Columns.list("id", "organizer_id", "title", "status"))
            filter {
                eq("id", id)
            }
        }.decodeSingle<DecideEventResponseDto>()

        return result
    }

    suspend fun approveEvent(id: Int): DecideEventResponseDto {
        val userId = auth.currentSessionOrNull()?.user?.id ?: throw Exception("User not authenticated")

        val result = postgrest.from("events").update(
            mapOf(
                "status" to "published",
                "approved_by" to userId,
                "approved_at" to "now()"
            )
        ) {
            select(Columns.list("id", "organizer_id", "title", "status"))
            filter {
                eq("id", id)

            }
        }.decodeSingle<DecideEventResponseDto>()

        return result
    }

    suspend fun getKycDocuments(businessId: String): List<GetKycDocumentDto> {
        return postgrest.from("kyc_documents").select {
            filter {
                eq("business_id", businessId)
            }
        }.decodeList<GetKycDocumentDto>()
    }

    suspend fun getTicketByCode(code: String): TicketDto {
        return postgrest.from("tickets").select {
            filter {
                eq("purchased_tickets", code)
            }
        }.decodeSingle<TicketDto>()
    }
}