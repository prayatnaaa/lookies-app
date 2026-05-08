package com.prayatna.lookiesapp.data.remote.api.supabase

import com.prayatna.lookiesapp.data.remote.dto.MerchantMemberDto
import com.prayatna.lookiesapp.data.remote.dto.MerchantProfileDto
import com.prayatna.lookiesapp.data.remote.dto.ShipmentDto
import com.prayatna.lookiesapp.data.remote.dto.request.merchant.InviteMerchantMemberRequest
import com.prayatna.lookiesapp.data.remote.dto.response.merchant.InviteMerchantMemberResponse
import io.github.jan.supabase.postgrest.Postgrest
import javax.inject.Inject

class SupabaseMerchantService @Inject constructor(
    private val postgrest: Postgrest
) {

    suspend fun inviteMerchantMember(request: InviteMerchantMemberRequest): InviteMerchantMemberResponse {
        return postgrest.from("merchant_members").insert(request) {
            select()
        }.decodeSingle<InviteMerchantMemberResponse>()
    }
    suspend fun getMerchantMembersByMerchantId(merchantBusinessId: String): List<MerchantMemberDto> {
        return postgrest
            .from("merchant_member_views")
            .select {
                filter {
                    eq("business_id", merchantBusinessId)
                }
            }.decodeList<MerchantMemberDto>()
    }
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

    suspend fun updateShipmentStatus(shipmentId: String, status: String): ShipmentDto {
        return postgrest
            .from("shipments")
            .update({
                set("status", status)
            }) {
                select()
                filter {
                    ShipmentDto::id eq shipmentId
                }
            }.decodeSingle<ShipmentDto>()
    }

    suspend fun createTrackingNumberShipment(shipmentId: String, trackingNumber: String): ShipmentDto {
        return postgrest
            .from("shipments")
            .update({
                set("tracking_number", trackingNumber)
            }) {
                select()
                filter {
                    ShipmentDto::id eq shipmentId
                }
            }.decodeSingle<ShipmentDto>()
    }

    suspend fun getShipmentsByMerchantId(merchantId: String): List<ShipmentDto> {
        return postgrest.from("shipments")
            .select {
                filter {
                    ShipmentDto::merchantId eq merchantId
                }
            }.decodeList<ShipmentDto>()
    }
}