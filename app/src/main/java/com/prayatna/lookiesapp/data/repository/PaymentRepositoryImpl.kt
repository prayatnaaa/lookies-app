package com.prayatna.lookiesapp.data.repository

import com.prayatna.lookiesapp.domain.model.payment.Payment
import com.prayatna.lookiesapp.data.remote.api.supabase.SupabasePaymentApi
import com.prayatna.lookiesapp.data.remote.request.payment.AddPaymentRequest
import com.prayatna.lookiesapp.data.remote.response.payment.AddPaymentResponse
import com.prayatna.lookiesapp.domain.repository.PaymentRepository
import com.prayatna.lookiesapp.utils.DataResult
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.gotrue.Auth
import javax.inject.Inject


class PaymentRepositoryImpl @Inject constructor(
    private val supabasePaymentApi: SupabasePaymentApi,
    private val auth: Auth
): PaymentRepository {
    override suspend fun addPayment(request: AddPaymentRequest): DataResult<AddPaymentResponse> {
        return try {

            val userId = auth.currentUserOrNull()?.id
                ?: return DataResult.Error("User not authenticated")
            val finalRequest = request.copy(userId = userId)

            val response = supabasePaymentApi.addPayment(finalRequest)
            DataResult.Success(response)
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