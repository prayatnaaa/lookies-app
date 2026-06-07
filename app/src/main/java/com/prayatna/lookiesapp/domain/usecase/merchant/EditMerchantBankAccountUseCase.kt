package com.prayatna.lookiesapp.domain.usecase.merchant

import com.prayatna.lookiesapp.domain.model.merchant.EditMerchantBankAccountInput
import com.prayatna.lookiesapp.domain.model.merchant.MerchantBankAccount
import com.prayatna.lookiesapp.domain.repository.MerchantRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class EditMerchantBankAccountUseCase @Inject constructor(
    private val repository: MerchantRepository
) {
    suspend operator fun invoke(id: String, input: EditMerchantBankAccountInput): DataResult<MerchantBankAccount> {
        return repository.updateMerchantBankAccount(id, input)
    }
}
