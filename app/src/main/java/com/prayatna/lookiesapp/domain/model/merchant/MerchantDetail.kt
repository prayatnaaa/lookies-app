package com.prayatna.lookiesapp.domain.model.merchant

data class MerchantDetail(

    val id: String,

    val type: String,

    val legalName: String,

    val tradingName: String? = null,

    val description: String? = null,

    val industryCategory: String,

    val dateOfRegistration: String? = null,

    val countryOfOperation: String,

    val websiteUrl: String? = null,

    val phoneNumber: String? = null,

    val email: String? = null,

    val pictureUrl: String? = null,

    val merchantType: String,

    val createdAt: String? = null,

    val updatedAt: String? = null,

    val merchantAccountId: String? = null,

    val kycStatus: String? = null,

    val kycRejectionReason: String? = null,

    val payoutEnabled: Boolean? = null,

    val ownerUserId: String? = null,

    val ownerEmail: String? = null,

    val ownerName: String? = null,

    val ownerPicture: String? = null,

    // RELATIONS
    val addresses: List<MerchantAddress> = emptyList(),

    val bankAccounts: List<MerchantBankAccount> = emptyList(),

    val members: List<Member> = emptyList(),

    val individuals: List<MerchantIndividual> = emptyList()
)

data class MerchantAddress(
    val id: String,
    val country: String,
    val province: String? = null,
    val city: String? = null,
    val district: String? = null,
    val subDistrict: String? = null,
    val streetLine1: String? = null,
    val streetLine2: String? = null,
    val postalCode: String? = null
)

data class MerchantBankAccount(
    val id: String,
    val bankCode: String,
    val bankName: String? = null,
    val accountNumber: String,
    val accountHolderName: String,
    val isPrimary: Boolean = false
)

data class Member(
    val userId: String,
    val name: String,
    val email: String,
    val role: String,
    val status: String
)

data class MerchantIndividual(
    val id: String,
    val givenNames: String,
    val surname: String,
    val email: String? = null,
    val phoneNumber: String? = null,
    val role: String? = null
)