package com.prayatna.lookiesapp.domain.model.merchant


data class MerchantMember(
    val id: String,
    val userId: String,
    val role: String,
    val status: String,
    val merchantAccountId: String,
    val businessId: String,
    val tradingName: String?,
    val username: String,
    val fullName: String,
    val kycStatus: String?,
    val createdAt: String,
    val updatedAt: String
)