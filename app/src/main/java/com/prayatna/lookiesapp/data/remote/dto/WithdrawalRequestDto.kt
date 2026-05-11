package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WithdrawalRequestDto(
    val id: String,
    @SerialName("merchant_id")
    val merchantId: String,
    val amount: Long,
    @SerialName("bank_code")
    val bankCode: String,
    @SerialName("account_number")
    val accountNumber: String,
    @SerialName("account_holder_name")
    val accountName: String,
    val status: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("admin_notes")
    val adminNotes: String? = null,
    @SerialName("xendit_disbursement_id")
    val xenditDisbursementId: String,
    @SerialName("updated_at")
    val updatedAt: String,
    @SerialName("processed_at")
    val processedAt: String
)
