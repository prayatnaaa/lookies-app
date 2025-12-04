package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PartnerDto(
    val id: String,
    val name: String,
    @SerialName("logo_url") val logoUrl: String,
    val status: String,
)
