package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MerchantProfileDto(

    @SerialName("account_id")
    val accountId: String,

    @SerialName("kyc_status")
    val kycStatus: String,

    @SerialName("payout_enabled")
    val payoutEnabled: Boolean,

    @SerialName("business_id")
    val businessId: String,

    @SerialName("legal_name")
    val legalName: String,

    @SerialName("trading_name")
    val tradingName: String? = null,

    @SerialName("picture_url")
    val pictureUrl: String? = null,

    @SerialName("description")
    val description: String? = null,

    @SerialName("merchant_type")
    val merchantType: String,

    @SerialName("email")
    val email: String? = null,

    @SerialName("phone_number")
    val phoneNumber: String? = null,

    @SerialName("website_url")
    val websiteUrl: String? = null
)