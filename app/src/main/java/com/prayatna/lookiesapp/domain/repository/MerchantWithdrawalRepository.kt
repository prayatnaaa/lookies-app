package com.prayatna.lookiesapp.domain.repository

import com.prayatna.lookiesapp.domain.model.withdrawal.WithdrawalRequest
import com.prayatna.lookiesapp.utils.DataResult

interface MerchantWithdrawalRepository {
    suspend fun getMerchantWithdrawalsRequest():
            DataResult<List<WithdrawalRequest>>
}