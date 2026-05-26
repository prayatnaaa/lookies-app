package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MerchantBusinessDto(
    @SerialName("id")
    val id: String,

    @SerialName("type")
    val type: String,

    @SerialName("legal_name")
    val legalName: String,

    @SerialName("trading_name")
    val tradingName: String? = null,

    @SerialName("description")
    val description: String? = null,

    @SerialName("industry_category")
    val industryCategory: String,

    @SerialName("date_of_registration")
    val dateOfRegistration: String? = null,

    @SerialName("country_of_operation")
    val countryOfOperation: String,

    @SerialName("website_url")
    val websiteUrl: String? = null,

    @SerialName("phone_number")
    val phoneNumber: String? = null,

    @SerialName("email")
    val email: String? = null,

    @SerialName("created_at")
    val createdAt: String? = null,

    @SerialName("updated_at")
    val updatedAt: String? = null,

    @SerialName("picture_url")
    val pictureUrl: String? = null,

    @SerialName("merchant_type")
    val merchantType: String,

    @SerialName("kyc_status")
    val status: String? = null
)