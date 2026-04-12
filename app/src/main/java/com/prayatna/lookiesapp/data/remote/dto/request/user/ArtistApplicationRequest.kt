package com.prayatna.lookiesapp.data.remote.dto.request.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArtistApplicationRequest(
    @SerialName("full_name")
    val fullName: String,

    @SerialName("display_name")
    val displayName: String? = null,

    @SerialName("bio")
    val bio: String? = null,

    @SerialName("phone_number")
    val phoneNumber: String,

    @SerialName("nationality")
    val nationality: String? = null,

    @SerialName("place_of_birth")
    val placeOfBirth: String? = null,

    @SerialName("date_of_birth")
    val dateOfBirth: String? = null,

    @SerialName("gender")
    val gender: GenderDto? = null,

    @SerialName("website")
    val website: String? = null,

    @SerialName("country")
    val country: String? = null,

    @SerialName("address")
    val address: AddressDto? = null,

    @SerialName("bank_account")
    val bankAccount: BankAccountDto,

    @SerialName("kyc_documents")
    val kycDocuments: List<KYCDocumentDto>? = null
)

@Serializable
data class AddressDto(
    @SerialName("country") val country: String,
    @SerialName("city") val city: String,
    @SerialName("street_line1") val streetLine1: String,
    @SerialName("street_line2") val streetLine2: String? = null,
    @SerialName("district") val district: String,
    @SerialName("sub_district") val subDistrict: String,
    @SerialName("province_state") val provinceState: String,
    @SerialName("postal_code") val postalCode: String
)

@Serializable
data class KYCDocumentDto(
    @SerialName("type") val type: String,
    @SerialName("country") val country: String,
    @SerialName("file_id") val fileId: String
)

@Serializable
enum class GenderDto {
    @SerialName("MALE") MALE,
    @SerialName("FEMALE") FEMALE
}