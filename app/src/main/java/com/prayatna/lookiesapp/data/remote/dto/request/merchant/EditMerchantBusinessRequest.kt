package com.prayatna.lookiesapp.data.remote.dto.request.merchant

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EditMerchantBusinessRequest(
    @SerialName("legal_name")
    val legalName: String? = null,
    @SerialName("trading_name")
    val tradingName: String? = null,
    @SerialName("description")
    val description: String? = null,
    @SerialName("industry_category")
    val industryCategory: String? = null,
    @SerialName("phone_number")
    val phoneNumber: String? = null,
    @SerialName("email")
    val email: String? = null,
    @SerialName("website_url")
    val websiteUrl: String? = null,
    @SerialName("picture_url")
    val pictureUrl: String? = null
)
