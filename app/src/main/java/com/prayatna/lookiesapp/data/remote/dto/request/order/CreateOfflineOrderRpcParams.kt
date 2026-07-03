package com.prayatna.lookiesapp.data.remote.dto.request.order

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateOfflineOrderRpcParams(
    @SerialName("p_buyer_id")
    val buyerId: String?,
    @SerialName("p_currency")
    val currency: String,
    @SerialName("p_items")
    val items: List<OrderItemRequest>
)
