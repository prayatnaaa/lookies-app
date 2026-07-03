package com.prayatna.lookiesapp.domain.model.merchant

data class MerchantProfile(
    val accountId: String,
    val kycStatus: String,
    val payoutEnabled: Boolean,
    val businessId: String,
    val legalName: String,
    val tradingName: String? = null,
    val pictureUrl: String? = null,
    val description: String? = null,
    val merchantType: String,
    val email: String? = null,
    val phoneNumber: String? = null,
    val websiteUrl: String? = null
)