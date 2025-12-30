package com.prayatna.lookiesapp.domain.repository

import com.prayatna.lookiesapp.domain.model.transaction.CheckoutOutput
import com.prayatna.lookiesapp.utils.DataResult

interface TransactionRepository {
    suspend fun createOrder(
        userId: String? = null,
        totalAmount: Double,
        orderType: String,
        description: String? = null,
        transactionType: String
    ): DataResult<CheckoutOutput>
}