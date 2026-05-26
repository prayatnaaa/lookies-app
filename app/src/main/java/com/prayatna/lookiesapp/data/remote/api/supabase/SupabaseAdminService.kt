package com.prayatna.lookiesapp.data.remote.api.supabase

import android.util.Log
import com.prayatna.lookiesapp.data.remote.dto.AdminTransactionDetailDto
import com.prayatna.lookiesapp.data.remote.dto.AdminTransactionDto
import com.prayatna.lookiesapp.data.remote.dto.GetKycDocumentDto
import com.prayatna.lookiesapp.data.remote.dto.TicketDto
import com.prayatna.lookiesapp.data.remote.dto.WithdrawalRequestDto
import com.prayatna.lookiesapp.data.remote.dto.response.admin.DecideEventResponseDto
import com.prayatna.lookiesapp.data.remote.dto.response.admin.DecidePartnerApplicationResponseDto
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
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

        Log.d("ApproveEvent", result.toString())

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
        return postgrest.from("purchased_tickets").select {
            filter {
                eq("ticket_code", code)
            }
        }.decodeSingle<TicketDto>()
    }

    suspend fun updateWithdrawalStatus(id: String, status: String, adminNotes: String?): WithdrawalRequestDto {
        return postgrest.from("withdrawal_requests").update(
            buildMap {
                put("status", status)
                put("updated_at", "now()")
                if (adminNotes != null) put("admin_notes", adminNotes)
                if (status == "completed") put("processed_at", "now()")
            }
        ) {
            select()
            filter {
                eq("id", id)
            }
        }.decodeSingle<WithdrawalRequestDto>()
    }

    suspend fun getTransactionList(limit: Int, offset: Int, status: String?): List<AdminTransactionDto> {
        return  postgrest["orders"]
            .select(
                columns = Columns.raw(
                    """
                        id, total_amount, status, created_at, 
                        users!orders_buyer_id_fkey1(email, user_profiles(full_name)), 
                        payment_attempts(status)
                        """.trimIndent()
                )
            ) {
                filter {
                    if (status != null) {
                        eq("status", status)
                    }
                }
                order("created_at", Order.DESCENDING)
                limit(count = limit.toLong())
                range(offset.toLong(), (offset + limit - 1).toLong())
            }.decodeList<AdminTransactionDto>()
    }

    suspend fun getTransactionDetail(orderId: String): AdminTransactionDetailDto {
        return postgrest["orders"]
            .select(
                columns = Columns.raw(
                    """
                        id, total_amount, status, created_at, 
                        users!orders_buyer_id_fkey1(email, user_profiles(full_name, phone_number)), 
                        order_items(id, item_type, unit_price, quantity, subtotal, event_id, event_painting_id), 
                        order_splits(id, merchant_id, gross_amount, platform_fee, net_amount, payout_status),
                        payment_attempts(status, provider, channel, external_id, created_at),
                        shipments(id, tracking_number, status, shipping_cost),
                        refund_requests(id, status, amount, reason)
                        """.trimIndent()
                )
            ) {
                filter {
                    eq("id", orderId)
                }
            }.decodeSingle<AdminTransactionDetailDto>()
    }
}