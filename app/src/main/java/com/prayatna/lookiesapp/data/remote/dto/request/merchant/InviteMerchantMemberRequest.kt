package com.prayatna.lookiesapp.data.remote.dto.request.merchant

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InviteMerchantMemberRequest(
    @SerialName("merchant_account_id")
    val merchantAccountId: String,
    @SerialName("user_id")
    val userId: String,
    val role: String,
    val status: String = "invited",
)