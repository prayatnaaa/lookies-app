package com.prayatna.lookiesapp.presentation.refund.createRefund.state

import android.net.Uri

data class CreateRefundFormState(
    val orderId: String = "",
    val amount: String = "",
    val bankCode: String = "",
    val accountNumber: String = "",
    val accountHolderName: String = "",
    val reason: String = "",
    val proofImageUri: Uri? = null
)
