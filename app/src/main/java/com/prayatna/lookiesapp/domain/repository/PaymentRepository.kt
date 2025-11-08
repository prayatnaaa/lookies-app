package com.prayatna.lookiesapp.domain.repository

import com.prayatna.lookiesapp.domain.model.payment.AddPayment
import com.prayatna.lookiesapp.domain.model.payment.AddPaymentResult
import com.prayatna.lookiesapp.domain.model.payment.Payment
import com.prayatna.lookiesapp.utils.DataResult

interface PaymentRepository {
    suspend fun addPayment(payment: AddPayment): DataResult<AddPaymentResult>
    fun getPaymentByUserId(): DataResult<Payment>
//    fun getPayments()
//    fun getPaymentsByUserId()
//    fun getPayment()
//    fun editPayment()
//    fun deletePayment()
}