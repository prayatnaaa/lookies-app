package com.prayatna.lookiesapp.data.remote.response.partner

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetPartnerDetailResponse(
    val partner: PartnerData,
    val documents: DocumentData,
    val location: LocationData? = null,
    val bank: BankData? = null,
    val profile: UserProfileData? = null
)

@Serializable
data class PartnerData(
    val id: Long,
    val name: String?,
    val type: String?,
    val logoUrl: String?,
    val portfolioLink: String?,
    val status: String,
    val createdAt: String
)

@Serializable
data class DocumentData(
    val ktpUrl: String?,
    val businessLicenseUrl: String?
)

@Serializable
data class LocationData(
    val id: Long,
    @SerialName("user_id")
    val userId: String,
    val name: String,
    val url: String,
)

@Serializable
data class BankData(
    val id: String,
    @SerialName("bank_name")
    val bankName: String?,
    @SerialName("bank_account_number")
    val bankAccountNumber: String?,
    @SerialName("bank_account_holder")
    val bankAccountHolder: String?,
    @SerialName("partner_id")
    val partnerId: String?
)

@Serializable
data class UserProfileData(
    @SerialName("user_id")
    val userId: String,
    @SerialName("full_name")
    val fullName: String?,
    val bio: String?,
    val address: String?,
    @SerialName("profile_picture_url")
    val profilePictureUrl: String?,
    val username: String?,
    @SerialName("has_partner_sub")
    val hasPartnerSub: Boolean?
)
