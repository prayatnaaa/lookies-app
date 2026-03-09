package com.prayatna.lookiesapp.domain.mapper

import com.prayatna.lookiesapp.data.remote.dto.MerchantMemberDto
import com.prayatna.lookiesapp.domain.model.merchant.MerchantMember

fun MerchantMemberDto.toDomain(): MerchantMember {
    return MerchantMember(
        id = id,
        userId = userId,
        role = role,
        status = status,
        merchantAccountId = merchantAccountId,
        businessId = businessId,
        tradingName = tradingName,
        kycStatus = kycStatus,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}