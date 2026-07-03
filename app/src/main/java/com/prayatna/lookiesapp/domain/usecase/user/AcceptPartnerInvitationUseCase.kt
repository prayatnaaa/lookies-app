package com.prayatna.lookiesapp.domain.usecase.user

import com.prayatna.lookiesapp.domain.model.merchant.AcceptPartnerInvitationResponse
import com.prayatna.lookiesapp.domain.repository.UserRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class AcceptPartnerInvitationUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(merchantAccountId: String): DataResult<AcceptPartnerInvitationResponse> {
        return userRepository.acceptPartnerInvitations(merchantAccountId)
    }
}
