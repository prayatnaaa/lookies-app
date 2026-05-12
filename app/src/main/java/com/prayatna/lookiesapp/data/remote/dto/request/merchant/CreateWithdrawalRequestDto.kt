package com.prayatna.lookiesapp.data.remote.dto.request.merchant

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateWithdrawalRequestDto(
    @SerialName("merchant_id")
    val merchantId: String,
    val amount: Long,
    @SerialName("bank_code")
    val bankCode: String,
    @SerialName("account_number")
    val accountNumber: String,
    @SerialName("account_holder_name")
    val accountName: String
)
