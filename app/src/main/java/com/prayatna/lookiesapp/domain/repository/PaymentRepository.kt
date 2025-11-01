package com.prayatna.lookiesapp.domain.repository

import com.prayatna.lookiesapp.data.remote.request.payment.AddPaymentRequest
import com.prayatna.lookiesapp.data.remote.response.payment.AddPaymentResponse
import com.prayatna.lookiesapp.domain.model.payment.Payment
import com.prayatna.lookiesapp.utils.DataResult

interface PaymentRepository {
    suspend fun addPayment(request: AddPaymentRequest): DataResult<AddPaymentResponse>
    fun getPaymentByUserId(): DataResult<Payment>
//    fun getPayments()
//    fun getPaymentsByUserId()
//    fun getPayment()
//    fun editPayment()
//    fun deletePayment()
}