package com.prayatna.lookiesapp.domain.repository

import com.prayatna.lookiesapp.domain.model.order.OrderItemInput
import com.prayatna.lookiesapp.domain.model.transaction.Transaction
import com.prayatna.lookiesapp.utils.DataResult

interface TransactionRepository {
    suspend fun createOrder(
        items: List<OrderItemInput>
    ): DataResult<String>
    suspend fun getUserTransactions(): DataResult<List<Transaction>>
}