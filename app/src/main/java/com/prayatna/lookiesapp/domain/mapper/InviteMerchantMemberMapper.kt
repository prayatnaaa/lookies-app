package com.prayatna.lookiesapp.domain.mapper

import com.prayatna.lookiesapp.data.remote.dto.request.merchant.InviteMerchantMemberRequest
import com.prayatna.lookiesapp.data.remote.dto.response.merchant.InviteMerchantMemberResponse
import com.prayatna.lookiesapp.domain.model.merchant.InviteMerchantMember
import com.prayatna.lookiesapp.domain.model.merchant.InviteMerchantMemberInput

fun InviteMerchantMemberInput.toData(): InviteMerchantMemberRequest {
    return InviteMerchantMemberRequest(
        merchantAccountId = merchantAccountId,
        userId = userId,
        role = role,
        status = status
    )
}

fun InviteMerchantMemberResponse.toDomain(): InviteMerchantMember {
    return InviteMerchantMember(
        id = id,
        merchantAccountId = merchantAccountId,
        userId = userId,
        role = role,
        status = status,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
