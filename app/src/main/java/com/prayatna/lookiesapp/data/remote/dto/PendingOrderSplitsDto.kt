package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PendingOrderSplitsDto(
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("total_amount")
    val totalAmount: Long
)