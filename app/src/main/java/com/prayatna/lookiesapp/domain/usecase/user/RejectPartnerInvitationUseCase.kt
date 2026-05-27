package com.prayatna.lookiesapp.domain.usecase.user

import com.prayatna.lookiesapp.domain.model.merchant.MerchantMember
import com.prayatna.lookiesapp.domain.repository.UserRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class RejectPartnerInvitationUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(merchantAccountId: String): DataResult<MerchantMember> {
        return userRepository.rejectPartnerInvitations(merchantAccountId)
    }
}
