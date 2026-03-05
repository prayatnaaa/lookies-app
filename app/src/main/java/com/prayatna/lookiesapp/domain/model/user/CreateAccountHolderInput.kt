package com.prayatna.lookiesapp.domain.model.user

data class RoleApplicationInput(
    val merchantType: String,
    val useLoginEmail: Boolean,
    val businessEmail: String? = null,
    val businessPayload: CreateAccountHolderInput,
    val bankAccounts: BankAccount
)

data class BankAccount(
    val bankCode: String,
    val bankName: String,
    val accountNumber: String,
    val accountHolderName: String,
    val isPrimary: Boolean? = null
)

data class CreateAccountHolderInput(
    val userId: String? = null,
    val businessDetail: BusinessDetail,
    val address: BusinessAddress,
    val individualDetails: List<IndividualDetail>,
    val kycDocuments: List<KycDocument>,
    val websiteUrl: String? = null,
    val phoneNumber: String? = null,
    val email: String? = null
)

data class BusinessDetail(
    val type: String,
    val legalName: String,
    val tradingName: String? = null,
    val description: String? = null,
    val industryCategory: String,
    val dateOfRegistration: String? = null,
    val countryOfOperation: String
)

data class BusinessAddress(
    val country: String,
    val city: String? = null,
    val streetLine1: String? = null,
    val streetLine2: String? = null,
    val district: String? = null,
    val subDistrict: String? = null,
    val provinceState: String? = null,
    val postalCode: String? = null
)

data class IndividualDetail(
    val givenNames: String,
    val surname: String,
    val phoneNumber: String? = null,
    val email: String? = null,
    val nationality: String? = null,
    val placeOfBirth: String? = null,
    val dateOfBirth: String? = null,
    val gender: String? = null,
    val type: String,
    val role: String? = null
)

data class KycDocument(
    val type: String,
    val country: String? = null,
    val fileId: String
)