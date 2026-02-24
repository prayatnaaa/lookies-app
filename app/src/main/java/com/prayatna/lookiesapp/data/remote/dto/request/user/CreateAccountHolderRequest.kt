package com.prayatna.lookiesapp.data.remote.dto.request.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class RoleApplicationRequest(
    @SerialName("use_login_email")
    val useLoginEmail: Boolean,

    @SerialName("business_email")
    val businessEmail: String? = null,

    @SerialName("business_payload")
    val businessPayload: CreateAccountHolderRequest
)

@Serializable
data class CreateAccountHolderRequest(
    @SerialName("user_id")
    val userId: String? = null,

    @SerialName("business_detail")
    val businessDetail: BusinessDetailDto,

    @SerialName("address")
    val address: BusinessAddressDto,

    @SerialName("individual_details")
    val individualDetails: List<IndividualDetailDto>,

    @SerialName("kyc_documents")
    val kycDocuments: List<KycDocumentDto>,

    @SerialName("website_url")
    val websiteUrl: String? = null,

    @SerialName("phone_number")
    val phoneNumber: String? = null,

    @SerialName("email")
    val email: String? = null
)

@Serializable
data class BusinessDetailDto(
    val type: String,

    @SerialName("legal_name")
    val legalName: String,

    @SerialName("trading_name")
    val tradingName: String? = null,

    val description: String? = null,

    @SerialName("industry_category")
    val industryCategory: String,

    @SerialName("date_of_registration")
    val dateOfRegistration: String? = null,

    @SerialName("country_of_operation")
    val countryOfOperation: String
)

@Serializable
data class BusinessAddressDto(
    val country: String,
    val city: String? = null,

    @SerialName("street_line1")
    val streetLine1: String? = null,

    @SerialName("street_line2")
    val streetLine2: String? = null,

    val district: String? = null,

    @SerialName("sub_district")
    val subDistrict: String? = null,

    @SerialName("province_state")
    val provinceState: String? = null,

    @SerialName("postal_code")
    val postalCode: String? = null
)

@Serializable
data class IndividualDetailDto(
    @SerialName("given_names")
    val givenNames: String,

    val surname: String,

    @SerialName("phone_number")
    val phoneNumber: String? = null,

    val email: String? = null,
    val nationality: String? = null,

    @SerialName("place_of_birth")
    val placeOfBirth: String? = null,

    @SerialName("date_of_birth")
    val dateOfBirth: String? = null,

    val gender: String? = null,
    val type: String,
    val role: String? = null
)

@Serializable
data class KycDocumentDto(
    val type: String,
    val country: String? = null,
    @SerialName("file_id")
    val fileId: String
)