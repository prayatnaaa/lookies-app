package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PendingOrderSplitsDto(
    @SerialName("created_at")
    val createdAt: String? = null,
    @SerialName("total_pending")
    val totalAmount: Long? = null
)