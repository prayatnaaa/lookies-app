package com.prayatna.lookiesapp.domain.repository

import com.prayatna.lookiesapp.domain.model.withdrawal.CreateWithdrawalRequestInput
import com.prayatna.lookiesapp.domain.model.withdrawal.WithdrawalRequest
import com.prayatna.lookiesapp.utils.DataResult

interface MerchantWithdrawalRepository {
    suspend fun getMerchantWithdrawalsRequest():
            DataResult<List<WithdrawalRequest>>
    suspend fun getWithdrawalRequestsByMerchantId(merchantId: String):
            DataResult<List<WithdrawalRequest>>
    suspend fun createWithdrawalRequest(input: CreateWithdrawalRequestInput):
            DataResult<WithdrawalRequest>
}