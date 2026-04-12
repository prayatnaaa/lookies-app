package com.prayatna.lookiesapp.domain.model.user

data class ArtistApplicationInput(
    val fullName: String,
    val displayName: String?,
    val bio: String?,
    val phoneNumber: String,
    val nationality: String?,
    val placeOfBirth: String?,
    val dateOfBirth: String?,
    val gender: Gender?,
    val website: String?,
    val country: String?,
    val address: Address?,
    val bankAccount: BankAccount,
    val kycDocuments: List<KYCDocument>?
)

data class Address(
    val country: String,
    val city: String,
    val streetLine1: String,
    val streetLine2: String?,
    val district: String,
    val subDistrict: String,
    val provinceState: String,
    val postalCode: String
)

data class KYCDocument(
    val type: String,
    val country: String,
    val fileId: String
)

enum class Gender {
    MALE, FEMALE
}