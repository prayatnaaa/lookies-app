package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.datetime.Instant

@Serializable
data class DetailPartnerDto(
    val success: Boolean,
    val data: PartnerData?
)

@Serializable
data class PartnerData(
    val name: String,
    val type: String,
    @SerialName("logo_url") val logoUrl: String?,
    @SerialName("location_id") val locationId: String?,
    @SerialName("portofolio_link") val portofolioLink: String?,
    val status: String,
    @SerialName("created_at") val createdAt: Instant,
    @SerialName("ktp_owner_url") val ktpOwnerUrl: String?,
    @SerialName("business_license_url") val businessLicenseUrl: String?,
    @SerialName("user_id") val userId: String,
    val id: String,
    val locations: List<GetLocationDto> = emptyList(),
    @SerialName("user_banks") val userBanks: List<UserBankDto> = emptyList()
)

@Serializable
data class GetLocationDto(
    val id: Int,
    val url: String?,
    val name: String,
    @SerialName("created_at") val createdAt: Instant,
    @SerialName("partner_id") val partnerId: String
)

@Serializable
data class UserBankDto(
    val id: String,
    @SerialName("bank_name") val bankName: String,
    @SerialName("partner_id") val partnerId: String,
    @SerialName("bank_account_holder") val bankAccountHolder: String,
    @SerialName("bank_account_number") val bankAccountNumber: String
)
