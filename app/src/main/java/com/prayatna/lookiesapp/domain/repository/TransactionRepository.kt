package com.prayatna.lookiesapp.domain.repository

import com.prayatna.lookiesapp.domain.model.order.OrderItemInput
import com.prayatna.lookiesapp.domain.model.payment.PaymentAttempt
import com.prayatna.lookiesapp.domain.model.transaction.CreateQrisPaymentRequestInput
import com.prayatna.lookiesapp.domain.model.transaction.CreateQrisPaymentRequestResult
import com.prayatna.lookiesapp.domain.model.transaction.CreateXenditPaymentRequestInput
import com.prayatna.lookiesapp.domain.model.transaction.CreateXenditPaymentRequestResult
import com.prayatna.lookiesapp.domain.model.transaction.Transaction
import com.prayatna.lookiesapp.utils.DataResult
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    suspend fun createOrder(items: List<OrderItemInput>):
            DataResult<String>
    suspend fun createQrisPaymentRequest(data: CreateQrisPaymentRequestInput):
            DataResult<CreateQrisPaymentRequestResult>
    suspend fun getUserTransactions():
            DataResult<List<Transaction>>
    suspend fun createPaymentRequest(request: CreateXenditPaymentRequestInput):
            DataResult<CreateXenditPaymentRequestResult>
    fun getPaymentAttempt(orderId: String):
            Flow<DataResult<PaymentAttempt>>
}