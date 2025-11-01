package com.prayatna.lookiesapp.domain.repository

import com.prayatna.lookiesapp.data.remote.request.payment.AddPaymentRequest
import com.prayatna.lookiesapp.data.remote.response.payment.AddPaymentResponse
import com.prayatna.lookiesapp.domain.model.Payment
import com.prayatna.lookiesapp.utils.DataResult

interface PaymentRepository {
    suspend fun addTicket(request: AddPaymentRequest): DataResult<AddPaymentResponse>
    fun getPaymentByUserId(): DataResult<Payment>
//    fun getPayments()
//    fun getPaymentsByUserId()
//    fun getPayment()
//    fun editPayment()
//    fun deletePayment()
}