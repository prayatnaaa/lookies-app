package com.prayatna.lookiesapp.domain.mapper

import com.prayatna.lookiesapp.data.remote.dto.MerchantProfileDto
import com.prayatna.lookiesapp.domain.model.merchant.MerchantProfile

fun MerchantProfileDto.toDomain(): MerchantProfile {
    return MerchantProfile(
        accountId = accountId,
        kycStatus = kycStatus,
        payoutEnabled = payoutEnabled,
        businessId = businessId,
        legalName = legalName,
        tradingName = tradingName,
        pictureUrl = pictureUrl,
        description = description,
        merchantType = merchantType
    )
}