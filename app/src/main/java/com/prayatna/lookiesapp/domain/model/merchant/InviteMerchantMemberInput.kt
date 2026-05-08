package com.prayatna.lookiesapp.domain.model.merchant

data class InviteMerchantMemberInput(
    val merchantAccountId: String,
    val userId: String,
    val role: String,
    val status: String = "invited"
)
