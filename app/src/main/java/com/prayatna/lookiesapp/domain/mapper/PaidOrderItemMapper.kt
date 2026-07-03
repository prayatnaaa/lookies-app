package com.prayatna.lookiesapp.domain.mapper

import com.prayatna.lookiesapp.data.remote.dto.PaidOrderItemDto
import com.prayatna.lookiesapp.domain.model.transaction.PaidOrderItem

fun PaidOrderItemDto.toDomain(): PaidOrderItem {
    return PaidOrderItem(
        orderItemId = orderItemId,
        eventId = eventId,
        itemType = itemType,
        quantity = quantity,
        unitPrice = unitPrice,
        subtotal = subtotal,
        orderId = orderId,
        paymentStatus = paymentStatus,
        paidAt = paidAt,
        buyerName = buyerName
    )
}
