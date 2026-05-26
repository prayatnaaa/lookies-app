package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserAddressDto(
    val id: String,
    @SerialName("user_id")
    val userId: String,
    @SerialName("recipient_name")
    val name: String,
    @SerialName("address_line")
    val addressLine: String,
    val province: String,
    @SerialName("phone_number")
    val phoneNumber: String,
    @SerialName("postal_code")
    val postalCode: String,
    val city: String,
    @SerialName("is_default")
    val isDefault: Boolean,
    val notes: String? = null,
    @SerialName("created_at")
    val createdAt: String
)