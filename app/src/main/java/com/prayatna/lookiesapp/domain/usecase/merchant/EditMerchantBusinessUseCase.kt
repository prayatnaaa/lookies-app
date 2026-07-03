package com.prayatna.lookiesapp.domain.usecase.merchant

import com.prayatna.lookiesapp.domain.model.merchant.EditMerchantInput
import com.prayatna.lookiesapp.domain.model.merchant.MerchantBusiness
import com.prayatna.lookiesapp.domain.repository.MerchantRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class EditMerchantBusinessUseCase @Inject constructor(
    private val repository: MerchantRepository
) {
    suspend operator fun invoke(id: String, input: EditMerchantInput): DataResult<MerchantBusiness> {
        return repository.updateMerchantBusiness(id, input)
    }
}
