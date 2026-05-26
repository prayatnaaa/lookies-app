package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MerchantDetailDto(

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

    @SerialName("picture_url")
    val pictureUrl: String? = null,

    @SerialName("merchant_type")
    val merchantType: String,

    @SerialName("created_at")
    val createdAt: String? = null,

    @SerialName("updated_at")
    val updatedAt: String? = null,

    // ACCOUNT
    @SerialName("merchant_account_id")
    val merchantAccountId: String? = null,

    @SerialName("kyc_status")
    val kycStatus: String? = null,

    @SerialName("kyc_rejection_reason")
    val kycRejectionReason: String? = null,

    @SerialName("payout_enabled")
    val payoutEnabled: Boolean? = null,

    // OWNER
    @SerialName("owner_user_id")
    val ownerUserId: String? = null,

    @SerialName("owner_email")
    val ownerEmail: String? = null,

    @SerialName("owner_name")
    val ownerName: String? = null,

    @SerialName("owner_picture")
    val ownerPicture: String? = null,

    // RELATIONS
    @SerialName("addresses")
    val addresses: List<MerchantAddressDto> = emptyList(),

    @SerialName("bank_accounts")
    val bankAccounts: List<MerchantBankAccountDto> = emptyList(),

    @SerialName("members")
    val members: List<MemberDto> = emptyList(),

    @SerialName("individuals")
    val individuals: List<MerchantIndividualDto> = emptyList()
)

@Serializable
data class MerchantAddressDto(
    val id: String,
    val country: String,
    val province: String? = null,
    val city: String? = null,
    val district: String? = null,
    @SerialName("sub_district")
    val subDistrict: String? = null,
    @SerialName("street_line1")
    val streetLine1: String? = null,
    @SerialName("street_line2")
    val streetLine2: String? = null,
    @SerialName("postal_code")
    val postalCode: String? = null
)

@Serializable
data class MerchantBankAccountDto(
    val id: String,
    @SerialName("bank_code")
    val bankCode: String,
    @SerialName("bank_name")
    val bankName: String? = null,
    @SerialName("account_number")
    val accountNumber: String,
    @SerialName("account_holder_name")
    val accountHolderName: String,
    @SerialName("is_primary")
    val isPrimary: Boolean = false
)

@Serializable
data class MemberDto(
    @SerialName("user_id")
    val userId: String,
    val name: String,
    val email: String,
    val role: String,
    val status: String
)

@Serializable
data class MerchantIndividualDto(
    val id: String,
    @SerialName("given_names")
    val givenNames: String,
    val surname: String,
    val email: String? = null,
    @SerialName("phone_number")
    val phoneNumber: String? = null,
    val role: String? = null
)