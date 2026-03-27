package com.prayatna.lookiesapp.data.remote.dto.response.admin

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DecidePartnerApplicationResponseDto(
    val id: String,
    @SerialName("business_id")
    val businessId: String,
    @SerialName("kyc_status")
    val status: String
)
