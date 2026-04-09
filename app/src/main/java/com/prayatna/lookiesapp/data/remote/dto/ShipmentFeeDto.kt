package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShipmentFeeDto(
    val id: String,
    val fee: Int,
    val region: String,
    @SerialName("created_at")
    val createdAt: String,
)