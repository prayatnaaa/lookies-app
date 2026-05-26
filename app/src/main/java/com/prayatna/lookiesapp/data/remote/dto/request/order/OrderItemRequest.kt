package com.prayatna.lookiesapp.data.remote.dto.request.order

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrderItemRequest(
    @SerialName("item_type")
    val itemType: String,
    @SerialName("item_ref_id")
    val itemRefId: String,
    val quantity: Int = 1
)
