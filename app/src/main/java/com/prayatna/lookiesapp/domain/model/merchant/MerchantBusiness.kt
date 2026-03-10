package com.prayatna.lookiesapp.domain.model.merchant

data class MerchantBusiness(
    val id: String,
    val type: String,
    val legalName: String,
    val tradingName: String?,
    val description: String?,
    val industryCategory: String,
    val dateOfRegistration: String?,
    val countryOfOperation: String,
    val websiteUrl: String?,
    val phoneNumber: String?,
    val email: String?,
    val createdAt: String?,
    val updatedAt: String?,
    val pictureUrl: String?,
    val merchantType: String,
    val status: String? = null
)