package com.prayatna.lookiesapp.domain.usecase.merchant

import com.prayatna.lookiesapp.domain.model.merchant.MerchantMember
import com.prayatna.lookiesapp.domain.repository.MerchantRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject


class GetMerchantMembersByMerchantIdUseCase @Inject constructor(
    private val merchantRepository: MerchantRepository
) {
    suspend operator fun invoke(merchantBusinessId: String): DataResult<List<MerchantMember>> {
        return merchantRepository.getMerchantMembersByMerchantId(merchantBusinessId)
    }
}