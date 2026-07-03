package com.prayatna.lookiesapp.domain.mapper

import com.prayatna.lookiesapp.data.remote.dto.response.user.AcceptPartnerInvitationResponseDto
import com.prayatna.lookiesapp.domain.model.merchant.AcceptPartnerInvitationResponse

fun AcceptPartnerInvitationResponseDto.toDomain(): AcceptPartnerInvitationResponse {
    return AcceptPartnerInvitationResponse(
        id = this.id,
        merchantAccountId = this.merchantAccountId,
        userId = this.userId,
        role = this.role,
        status = this.status,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}
