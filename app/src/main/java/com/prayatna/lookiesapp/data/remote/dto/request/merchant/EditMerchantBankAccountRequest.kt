package com.prayatna.lookiesapp.data.remote.dto.request.merchant

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EditMerchantBankAccountRequest(
    @SerialName("bank_code")
    val bankCode: String? = null,
    @SerialName("bank_name")
    val bankName: String? = null,
    @SerialName("account_number")
    val accountNumber: String? = null,
    @SerialName("account_holder_name")
    val accountHolderName: String? = null,
    @SerialName("is_primary")
    val isPrimary: Boolean? = null
)
