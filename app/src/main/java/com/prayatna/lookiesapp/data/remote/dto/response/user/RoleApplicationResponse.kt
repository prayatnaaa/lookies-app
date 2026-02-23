package com.prayatna.lookiesapp.data.remote.dto.response.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RoleApplicationResponse(
    val message: String,
    val status: String,
    @SerialName("business_id")
    val businessId: String? = null
)
