package com.prayatna.lookiesapp.data.repository

import com.prayatna.lookiesapp.domain.model.payment.Payment
import com.prayatna.lookiesapp.data.remote.api.supabase.SupabasePaymentService
import com.prayatna.lookiesapp.data.mapper.asDomainModel
import com.prayatna.lookiesapp.data.remote.request.payment.AddPaymentRequest
import com.prayatna.lookiesapp.domain.model.payment.AddPayment
import com.prayatna.lookiesapp.domain.model.payment.AddPaymentResult
import com.prayatna.lookiesapp.domain.repository.PaymentRepository
import com.prayatna.lookiesapp.utils.DataResult
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.gotrue.Auth
import javax.inject.Inject


class PaymentRepositoryImpl @Inject constructor(
    private val supabasePaymentService: SupabasePaymentService,
    private val auth: Auth,
): PaymentRepository {
    override suspend fun addPayment(payment: AddPayment): DataResult<AddPaymentResult> {
        return try {

            val userId = auth.currentUserOrNull()?.id
                ?: return DataResult.Error("User not authenticated")

            val token = auth.currentSessionOrNull()?.accessToken
                ?: return DataResult.Error("Missing auth token")

            val request = AddPaymentRequest(
                userId = userId,
                amount = payment.amount,
                paymentType = "event_ticket",
                quantity = payment.quantity
            )
            val response = supabasePaymentService.addPayment(request = request, token = token)
            DataResult.Success(
                AddPaymentResult(
                    message = response.message,
                    data = response.data?.asDomainModel()
                )
            )
        } catch (e: RestException) {
            DataResult.Error(e.error)
        } catch (e: Exception) {
            DataResult.Error(e.message.toString())
        }
    }

    override fun getPaymentByUserId(): DataResult<Payment> {
        TODO("Not yet implemented")
    }

}