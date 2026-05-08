package com.prayatna.lookiesapp.domain.usecase.merchant

import com.prayatna.lookiesapp.domain.model.merchant.InviteMerchantMemberInput
import com.prayatna.lookiesapp.domain.model.merchant.InviteMerchantMemberOutput
import com.prayatna.lookiesapp.domain.repository.MerchantRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class InviteMerchantMemberUseCase @Inject constructor(
    private val merchantRepository: MerchantRepository
) {
    suspend operator fun invoke(input: InviteMerchantMemberInput): DataResult<InviteMerchantMemberOutput> {
        return merchantRepository.inviteMerchantMember(input)
    }
}
