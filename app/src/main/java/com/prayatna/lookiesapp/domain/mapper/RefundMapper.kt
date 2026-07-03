package com.prayatna.lookiesapp.domain.mapper

import com.prayatna.lookiesapp.data.remote.dto.DetailRefundDto
import com.prayatna.lookiesapp.data.remote.dto.RefundDto
import com.prayatna.lookiesapp.domain.model.transaction.DetailRefund
import com.prayatna.lookiesapp.domain.model.transaction.Refund

fun RefundDto.toDomain(): Refund {
    return Refund(
        id = id,
        orderId = orderId,
        userId = userId,
        amount = amount,
        bankCode = bankCode,
        accountNumber = accountNumber,
        accountHolderName = accountHolderName,
        reason = reason,
        proofImageUrl = proofImageUrl,
        returnTrackingNumber = returnTrackingNumber,
        status = status,
        adminNotes = adminNotes,
        xenditPayoutId = xenditPayoutId,
        createdAt = createdAt.toString(),
        updatedAt = updatedAt.toString()
    )
}

fun DetailRefundDto.toDomain(): DetailRefund {
    return DetailRefund(
        id = id,
        orderId = orderId,
        userId = userId,
        amount = amount,
        bankCode = bankCode,
        accountNumber = accountNumber,
        accountHolderName = accountHolderName,
        reason = reason,
        proofImageUrl = proofImageUrl,
        returnTrackingNumber = returnTrackingNumber,
        status = status,
        adminNotes = adminNotes,
        xenditPayoutId = xenditPayoutId,
        createdAt = createdAt,
        updatedAt = updatedAt,
        streetLine1 = streetLine1,
        streetLine2 = streetLine2,
        city = city,
        district = district,
        subDistrict = subDistrict,
        provinceState = provinceState,
        postalCode = postalCode,
        country = country
    )
}
