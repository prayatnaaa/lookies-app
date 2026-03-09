package com.prayatna.lookiesapp.domain.usecase.merchant

import com.prayatna.lookiesapp.domain.model.merchant.MerchantProfile
import com.prayatna.lookiesapp.domain.repository.MerchantRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class GetMerchantProfileUseCase @Inject constructor(
    private val merchantRepository: MerchantRepository
) {
    suspend operator fun invoke(businessId: String): DataResult<MerchantProfile> {
        return merchantRepository.getMerchantProfile(businessId)
    }
}