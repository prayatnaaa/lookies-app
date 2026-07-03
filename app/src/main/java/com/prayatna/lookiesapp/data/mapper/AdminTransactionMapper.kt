package com.prayatna.lookiesapp.data.mapper

import com.prayatna.lookiesapp.data.remote.dto.*
import com.prayatna.lookiesapp.domain.model.admin.*

fun AdminTransactionDto.toDomain(): AdminTransaction {
    return AdminTransaction(
        id = id,
        totalAmount = totalAmount,
        status = status,
        createdAt = createdAt,
        userEmail = user?.email,
        userFullName = user?.profile?.fullName,
        paymentStatus = paymentAttempts.firstOrNull()?.status
    )
}

fun AdminTransactionDetailDto.toDomain(): AdminTransactionDetail {
    return AdminTransactionDetail(
        id = id,
        totalAmount = totalAmount,
        status = status,
        createdAt = createdAt,
        userEmail = user?.email,
        userFullName = user?.profile?.fullName,
        items = items.map { it.toDomain() },
        splits = splits.map { it.toAdminDomain() },
        paymentAttempts = paymentAttempts.map { it.toDomain() },
        shipments = shipments.map { it.toAdminDomain() },
        refundRequests = refundRequests.map { it.toDomain() }
    )
}

fun OrderItemDetailDto.toDomain(): AdminOrderItem {
    return AdminOrderItem(
        id = id,
        itemType = itemType,
        unitPrice = unitPrice,
        quantity = quantity,
        subtotal = subtotal,
        eventId = eventId,
        eventPaintingId = eventPaintingId
    )
}

fun OrderSplitDto.toAdminDomain(): AdminOrderSplit {
    return AdminOrderSplit(
        id = id,
        merchantId = merchantId,
        grossAmount = grossAmount,
        platformFee = platformFee,
        netAmount = netAmount,
        payoutStatus = payoutStatus
    )
}

fun PaymentAttemptDetailDto.toDomain(): AdminPaymentAttempt {
    return AdminPaymentAttempt(
        status = status,
        provider = provider,
        channel = channel,
        externalId = externalId,
        createdAt = createdAt
    )
}

fun ShipmentDto.toAdminDomain(): AdminShipment {
    return AdminShipment(
        id = id,
        trackingNumber = trackingNumber,
        status = status,
        shippingCost = shippingCost
    )
}

fun RefundRequestDto.toDomain(): AdminRefundRequest {
    return AdminRefundRequest(
        id = id,
        status = status,
        amount = amount,
        reason = reason,
    )
}
