package com.prayatna.lookiesapp.domain.usecase.withdrawal

import com.prayatna.lookiesapp.domain.model.withdrawal.WithdrawalRequest
import com.prayatna.lookiesapp.domain.repository.MerchantWithdrawalRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class GetMerchantWithdrawalRequestsUseCase @Inject constructor(
    private val merchantWithdrawalRepository: MerchantWithdrawalRepository
) {
    suspend operator fun invoke(): DataResult<List<WithdrawalRequest>> {
        return merchantWithdrawalRepository.getMerchantWithdrawalsRequest()
    }
}