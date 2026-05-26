package com.prayatna.lookiesapp.domain.usecase.merchant

import com.prayatna.lookiesapp.domain.model.merchant.MerchantBankAccount
import com.prayatna.lookiesapp.domain.repository.MerchantRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class GetMerchantBankAccountsUseCase @Inject constructor(
    private val repository: MerchantRepository
) {
    suspend operator fun invoke(merchantAccountId: String): DataResult<List<MerchantBankAccount>> {
        return repository.getMerchantBankAccounts(merchantAccountId)
    }
}
