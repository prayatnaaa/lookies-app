package com.prayatna.lookiesapp.domain.usecase.merchant

import com.prayatna.lookiesapp.domain.model.merchant.MerchantBusiness
import com.prayatna.lookiesapp.domain.repository.MerchantRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class GetPublicMerchantProfileUseCase @Inject constructor(
    private val repository: MerchantRepository
) {
    suspend operator fun invoke(businessId: String): DataResult<MerchantBusiness> {
        return repository.getPublicMerchantProfile(businessId)
    }
}
