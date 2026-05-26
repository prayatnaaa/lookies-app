package com.prayatna.lookiesapp.domain.usecase.withdrawal

import com.prayatna.lookiesapp.domain.model.withdrawal.WithdrawalRequest
import com.prayatna.lookiesapp.domain.repository.MerchantWithdrawalRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class GetWithdrawalRequestByIdUseCase @Inject constructor(
    private val repository: MerchantWithdrawalRepository
) {
    suspend operator fun invoke(id: String): DataResult<WithdrawalRequest> {
        return repository.getWithdrawalRequestById(id)
    }
}
