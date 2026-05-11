package com.prayatna.lookiesapp.domain.mapper

import com.prayatna.lookiesapp.data.remote.dto.WithdrawalRequestDto
import com.prayatna.lookiesapp.domain.model.withdrawal.WithdrawalRequest

fun WithdrawalRequestDto.toDomain(): WithdrawalRequest {
    return WithdrawalRequest(
        id = id,
        merchantId = merchantId,
        amount = amount,
        bankCode = bankCode,
        accountNumber = accountNumber,
        accountName = accountName,
        status = status,
        createdAt = createdAt,
        adminNotes = adminNotes,
        xenditDisbursementId = xenditDisbursementId,
        updatedAt = updatedAt,
        processedAt = processedAt
    )
}
