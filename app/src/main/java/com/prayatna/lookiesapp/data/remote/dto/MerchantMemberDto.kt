package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class MerchantMemberDto(
    val id: String,
    @SerialName("user_id")
    val userId: String,
    val role: String,
    val status: String,
    @SerialName("merchant_account_id")
    val merchantAccountId: String,
    @SerialName("business_id")
    val businessId: String,
    @SerialName("trading_name")
    val tradingName: String?,
    @SerialName("kyc_status")
    val kycStatus: String?,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String
)