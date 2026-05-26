package com.prayatna.lookiesapp.domain.model.merchant

data class InviteMerchantMemberOutput(
    val id: String,
    val merchantAccountId: String,
    val userId: String,
    val role: String,
    val status: String,
    val createdAt: String,
    val updatedAt: String
)
