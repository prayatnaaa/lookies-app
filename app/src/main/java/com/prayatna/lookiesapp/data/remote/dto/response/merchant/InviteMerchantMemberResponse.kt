package com.prayatna.lookiesapp.data.remote.dto.response.merchant

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InviteMerchantMemberResponse(
    val id: String,
    @SerialName("merchant_account_id")
    val merchantAccountId: String,
    @SerialName("user_id")
    val userId: String,
    val role: String,
    val status: String = "invited",
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String
)