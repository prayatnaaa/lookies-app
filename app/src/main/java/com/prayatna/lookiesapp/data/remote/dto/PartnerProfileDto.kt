package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PartnerProfileDto(
    val id: String,
    val name: String,

    @SerialName("logo_url")
    val logoUrl: String? = null,

    val type: String? = null
)
