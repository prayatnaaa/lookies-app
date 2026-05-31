package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BusinessAddressDto(

    @SerialName("id")
    val id: String? = null,

    @SerialName("business_id")
    val businessId: String,

    @SerialName("country")
    val country: String,

    @SerialName("city")
    val city: String? = null,

    @SerialName("street_line1")
    val streetLine1: String? = null,

    @SerialName("street_line2")
    val streetLine2: String? = null,

    @SerialName("district")
    val district: String? = null,

    @SerialName("sub_district")
    val subDistrict: String? = null,

    @SerialName("province_state")
    val provinceState: String? = null,

    @SerialName("postal_code")
    val postalCode: String? = null,

    @SerialName("created_at")
    val createdAt: String? = null
)