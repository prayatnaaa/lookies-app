package com.prayatna.lookiesapp.data.mapper

import com.prayatna.lookiesapp.data.remote.dto.ItemDetailDto
import com.prayatna.lookiesapp.data.remote.dto.PaymentInfoDto
import com.prayatna.lookiesapp.data.remote.dto.TransactionDto
import com.prayatna.lookiesapp.data.remote.dto.TransactionItemDto
import com.prayatna.lookiesapp.domain.model.transaction.ItemDetail
import com.prayatna.lookiesapp.domain.model.transaction.PaymentInfo
import com.prayatna.lookiesapp.domain.model.transaction.Transaction
import com.prayatna.lookiesapp.domain.model.transaction.TransactionItem

fun TransactionDto.toDomain(): Transaction {
    return Transaction(
        id = orderId,
        buyerId = buyerId,
        totalAmount = totalAmount,
        currency = currency,
        status = status,
        createdAt = createdAt,
        paymentInfo = paymentInfo?.toDomain(),
        items = items.map { it.toDomain() },
        merchantId = merchantId
    )
}

fun TransactionItemDto.toDomain(): TransactionItem {
    return TransactionItem(
        id = id,
        itemType = itemType,
        quantity = quantity,
        unitPrice = unitPrice,
        subtotal = subtotal,
        details = details?.toDomain(),
        itemRefId = itemRefId
    )
}

fun PaymentInfoDto.toDomain(): PaymentInfo {
    return PaymentInfo(
        paymentId = paymentId,
        invoiceId = invoiceId,
        status = status,
        provider = provider,
        channel = channel
    )
}

fun ItemDetailDto.toDomain(): ItemDetail {
    return ItemDetail(
        title = title,
        imageUrl = imageUrl,
        artistName = artistName,
        eventDate = eventDate
    )
}