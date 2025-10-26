package com.prayatna.lookiesapp.data.repository

import com.prayatna.lookiesapp.data.model.Payment
import com.prayatna.lookiesapp.data.remote.api.supabase.SupabasePaymentApi
import com.prayatna.lookiesapp.data.remote.request.payment.AddPaymentRequest
import com.prayatna.lookiesapp.data.remote.response.payment.AddPaymentResponse
import com.prayatna.lookiesapp.utils.DataResult
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.gotrue.Auth
import javax.inject.Inject

interface PaymentRepository {
    suspend fun addTicket(request: AddPaymentRequest): DataResult<AddPaymentResponse>
    fun getPaymentByUserId(): DataResult<Payment>
//    fun getPayments()
//    fun getPaymentsByUserId()
//    fun getPayment()
//    fun editPayment()
//    fun deletePayment()
}

class PaymentRepositoryImpl @Inject constructor(
    private val supabasePaymentApi: SupabasePaymentApi,
    private val auth: Auth
): PaymentRepository {
    override suspend fun addTicket(request: AddPaymentRequest): DataResult<AddPaymentResponse> {
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