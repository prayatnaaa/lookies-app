package com.prayatna.lookiesapp.domain.usecase.merchant

import com.prayatna.lookiesapp.domain.model.user.BusinessAddress
import com.prayatna.lookiesapp.domain.repository.MerchantRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class GetMerchantAddressUseCase @Inject constructor(
    private val repository: MerchantRepository
) {
    suspend operator fun invoke(merchantBusinessId: String): DataResult<BusinessAddress> {
        return repository.getMerchantAddress(merchantBusinessId)
    }
}
