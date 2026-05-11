package com.prayatna.lookiesapp.data.remote.api.supabase

import com.prayatna.lookiesapp.data.remote.dto.WithdrawalRequestDto
import io.github.jan.supabase.postgrest.Postgrest
import javax.inject.Inject

class SupabaseMerchantWithdrawalService @Inject constructor(
    private val postgrest: Postgrest
) {
    suspend fun getWithdrawalRequests(): List<WithdrawalRequestDto> {
        return postgrest.from("withdrawal_requests")
            .select()
            .decodeList<WithdrawalRequestDto>()
    }
}