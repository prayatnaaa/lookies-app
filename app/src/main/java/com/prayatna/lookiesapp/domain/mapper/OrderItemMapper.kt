package com.prayatna.lookiesapp.domain.mapper

import com.prayatna.lookiesapp.data.remote.dto.request.order.OrderItemRequest
import com.prayatna.lookiesapp.domain.model.order.OrderItemInput

fun OrderItemInput.toDto(): OrderItemRequest {
    return OrderItemRequest(
        itemType = itemType,
        itemRefId = itemRefId,
        quantity = quantity
    )
}