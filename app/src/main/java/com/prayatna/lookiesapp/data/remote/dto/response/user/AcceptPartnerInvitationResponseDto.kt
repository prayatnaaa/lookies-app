package com.prayatna.lookiesapp.data.remote.dto.response.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AcceptPartnerInvitationResponseDto(
    @SerialName("id")
    val id: String,
    @SerialName("merchant_account_id")
    val merchantAccountId: String,
    @SerialName("user_id")
    val userId: String,
    @SerialName("role")
    val role: String,
    @SerialName("status")
    val status: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String
)
