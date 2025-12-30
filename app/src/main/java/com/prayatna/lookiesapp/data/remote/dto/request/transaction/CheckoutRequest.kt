package com.prayatna.lookiesapp.data.remote.dto.request.transaction

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateOrderRequest(
    @SerialName("p_user_id")
    val userId: String,

    @SerialName("p_total_amount")
    val totalAmount: Double,

    @SerialName("p_order_type")
    val orderType: String,

    @SerialName("p_description")
    val description: String? = null,

    @SerialName("p_transaction_type")
    val transactionType: String
)
