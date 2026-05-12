package com.prayatna.lookiesapp.data.remote.api.supabase

import com.prayatna.lookiesapp.data.remote.dto.WithdrawalRequestDto
import com.prayatna.lookiesapp.data.remote.dto.request.merchant.CreateWithdrawalRequestDto
import io.github.jan.supabase.postgrest.Postgrest
import javax.inject.Inject

class SupabaseMerchantWithdrawalService @Inject constructor(
    private val postgrest: Postgrest
) {
    suspend fun createWithdrawalRequest(request: CreateWithdrawalRequestDto): WithdrawalRequestDto {
        return postgrest.from("withdrawal_requests").insert(request) {
            select()
        }.decodeSingle<WithdrawalRequestDto>()
    }

    suspend fun getWithdrawalRequests(): List<WithdrawalRequestDto> {
        return postgrest.from("withdrawal_requests")
            .select()
            .decodeList<WithdrawalRequestDto>()
    }

    suspend fun getWithdrawalRequestsByMerchantId(merchantId: String): List<WithdrawalRequestDto> {
        return postgrest.from("withdrawal_requests")
            .select {
                filter {
                    eq("merchant_id", merchantId)
                }
            }
            .decodeList<WithdrawalRequestDto>()
    }
}