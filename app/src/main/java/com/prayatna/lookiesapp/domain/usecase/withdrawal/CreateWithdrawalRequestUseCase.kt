package com.prayatna.lookiesapp.domain.usecase.withdrawal

import com.prayatna.lookiesapp.domain.model.withdrawal.CreateWithdrawalRequestInput
import com.prayatna.lookiesapp.domain.model.withdrawal.WithdrawalRequest
import com.prayatna.lookiesapp.domain.repository.MerchantWithdrawalRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class CreateWithdrawalRequestUseCase @Inject constructor(
    private val merchantWithdrawalRepository: MerchantWithdrawalRepository
) {
    suspend operator fun invoke(input: CreateWithdrawalRequestInput): DataResult<WithdrawalRequest> {
        return merchantWithdrawalRepository.createWithdrawalRequest(input)
    }
}
