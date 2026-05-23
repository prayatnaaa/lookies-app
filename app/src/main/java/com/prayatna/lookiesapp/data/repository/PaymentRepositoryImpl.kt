package com.prayatna.lookiesapp.data.repository

import com.prayatna.lookiesapp.data.mapper.asDomainModel
import com.prayatna.lookiesapp.data.remote.api.supabase.SupabasePaymentService
import com.prayatna.lookiesapp.data.remote.api.xendit.XenditService
import com.prayatna.lookiesapp.data.remote.dto.request.payment.AddPaymentRequest
import com.prayatna.lookiesapp.domain.mapper.toDomain
import com.prayatna.lookiesapp.domain.model.payment.AddPayment
import com.prayatna.lookiesapp.domain.model.payment.AddPaymentResult
import com.prayatna.lookiesapp.domain.model.payment.Payment
import com.prayatna.lookiesapp.domain.model.payment.PayoutChannel
import com.prayatna.lookiesapp.domain.repository.PaymentRepository
import com.prayatna.lookiesapp.utils.DataResult
import com.prayatna.lookiesapp.utils.extractSupabaseError
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.gotrue.Auth
import javax.inject.Inject


class PaymentRepositoryImpl @Inject constructor(
    private val supabasePaymentService: SupabasePaymentService,
    private val xenditService: XenditService,
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
            val msg = extractSupabaseError(e.error)
            DataResult.Error(msg)
        } catch (e: HttpRequestException) {
            DataResult.Error(e.message ?: "Network error")
        } catch (e: Exception) {
            DataResult.Error("Something went wrong! Please check your connection")
        }
    }

    override fun getPaymentByUserId(): DataResult<Payment> {
        TODO("Not yet implemented")
    }

    override suspend fun getPayoutChannels(): DataResult<List<PayoutChannel>> {
        return try {
            val response = xenditService.getPayoutChannels()
            DataResult.Success(response.map { it.toDomain() })
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "Failed to fetch payout channels")
        }
    }

}
