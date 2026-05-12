package com.prayatna.lookiesapp.data.repository

import com.prayatna.lookiesapp.data.remote.api.supabase.SupabaseMerchantWithdrawalService
import com.prayatna.lookiesapp.domain.mapper.toDto
import com.prayatna.lookiesapp.domain.mapper.toDomain
import com.prayatna.lookiesapp.domain.model.withdrawal.CreateWithdrawalRequestInput
import com.prayatna.lookiesapp.domain.model.withdrawal.WithdrawalRequest
import com.prayatna.lookiesapp.domain.repository.MerchantWithdrawalRepository
import com.prayatna.lookiesapp.utils.DataResult
import com.prayatna.lookiesapp.utils.extractSupabaseError
import io.github.jan.supabase.exceptions.RestException
import javax.inject.Inject

class MerchantWithdrawalRepositoryImpl @Inject constructor(
    private val supabaseMerchantWithdrawalService: SupabaseMerchantWithdrawalService
): MerchantWithdrawalRepository
{
    override suspend fun getMerchantWithdrawalsRequest(): DataResult<List<WithdrawalRequest>> {
        return try {
            val res = supabaseMerchantWithdrawalService.getWithdrawalRequests()
            DataResult.Success(res.map { it.toDomain() })
        } catch (e: RestException) {
            val msg = extractSupabaseError(e.error)
            DataResult.Error(msg)
        }
    }

    override suspend fun getWithdrawalRequestsByMerchantId(merchantId: String): DataResult<List<WithdrawalRequest>> {
        return try {
            val res = supabaseMerchantWithdrawalService.getWithdrawalRequestsByMerchantId(merchantId)
            DataResult.Success(res.map { it.toDomain() })
        } catch (e: RestException) {
            val msg = extractSupabaseError(e.error)
            DataResult.Error(msg)
        }
    }

    override suspend fun createWithdrawalRequest(input: CreateWithdrawalRequestInput): DataResult<WithdrawalRequest> {
        return try {
            val res = supabaseMerchantWithdrawalService.createWithdrawalRequest(input.toDto())
            DataResult.Success(res.toDomain())
        } catch (e: RestException) {
            val msg = extractSupabaseError(e.error)
            DataResult.Error(msg)
        }
    }
}