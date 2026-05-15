package com.prayatna.lookiesapp.domain.model.transaction

data class PayoutResult(
    val status: String,
    val message: String,
    val withdrawalId: String? = null,
    val payoutId: String? = null,
    val xenditStatus: String? = null
)
