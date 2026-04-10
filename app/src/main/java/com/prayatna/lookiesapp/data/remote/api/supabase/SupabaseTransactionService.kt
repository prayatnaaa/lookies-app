package com.prayatna.lookiesapp.data.remote.api.supabase

import android.util.Log
import com.prayatna.lookiesapp.BuildConfig
import com.prayatna.lookiesapp.data.remote.dto.PaymentAttemptDto
import com.prayatna.lookiesapp.data.remote.dto.ShipmentDto
import com.prayatna.lookiesapp.data.remote.dto.ShipmentFeeDto
import com.prayatna.lookiesapp.data.remote.dto.TicketDto
import com.prayatna.lookiesapp.data.remote.dto.TransactionDto
import com.prayatna.lookiesapp.data.remote.dto.request.order.CreateOrderRpcParams
import com.prayatna.lookiesapp.data.remote.dto.request.order.OrderItemRequest
import com.prayatna.lookiesapp.data.remote.dto.request.payment.CreateQrisPaymentRequestRequest
import com.prayatna.lookiesapp.data.remote.dto.request.payment.CreateXenditPaymentRequest
import com.prayatna.lookiesapp.data.remote.dto.response.payment.CreateQrisPaymentRequestResponse
import com.prayatna.lookiesapp.data.remote.dto.response.payment.CreateXenditPaymentResponse
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.postgrest.rpc
import io.github.jan.supabase.realtime.selectSingleValueAsFlow
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SupabaseTransactionService @Inject constructor(
    private val auth: Auth,
    private val postgrest: Postgrest,
    private val httpClient: HttpClient
) {
    suspend fun createOrder(
        items: List<OrderItemRequest>,
        shippingCost: Double,
        recipientName: String,
        phoneNumber: String,
        addressLine: String,
        province: String,
        postalCode: String
    ): String {
        val userId = auth.currentUserOrNull()?.id
            ?: throw IllegalStateException("User not authenticated")

        val params = CreateOrderRpcParams(
            buyerId = userId,
            items = items,
            shippingCost = shippingCost.toLong(),
            recipientName = recipientName,
            phoneNumber = phoneNumber,
            addressLine = addressLine,
            province = province,
            postalCode = postalCode.toInt()
        )

        val orderId = postgrest.rpc(
            function = "v3_create_order_with_items",
            parameters = params
        ).decodeAs<String>()

        Log.d("OrderService", "Order created with ID: $orderId")

        return orderId
    }

    suspend fun getUserTransactions(): List<TransactionDto> {
        val user = auth.currentUserOrNull() ?: throw IllegalStateException("User not logged in")

        val result = postgrest
            .from("transactions_view")
            .select {
                filter {
                    eq("buyer_id", user.id)
                }
                order("created_at", order = Order.DESCENDING)
            }
            .decodeList<TransactionDto>()
        Log.d("SupabaseTransactionService", "getUserTransactions: $result")
        return result
    }

    suspend fun getUserTransactionByOrderId(orderId: String): TransactionDto {
        val user = auth.currentUserOrNull() ?: throw IllegalStateException("User not logged in")

        val result = postgrest
            .from("transactions_view")
            .select {
                filter {
                    eq("buyer_id", user.id)
                    eq("order_id", orderId)
                }
                order("created_at", order = Order.DESCENDING)
            }
            .decodeSingle<TransactionDto>()
        return result

    }

    suspend fun createPaymentRequest(request: CreateXenditPaymentRequest): CreateXenditPaymentResponse {
        val session = auth.currentSessionOrNull()
            ?: throw IllegalStateException("No active session")

        val response =  httpClient.post(
            "${BuildConfig.SUPABASE_EDGE_BASE_URL}/create-payment"
        ) {
            contentType(ContentType.Application.Json)
            header("Authorization", "Bearer ${session.accessToken}")
            setBody(request)
        }
        Log.d("CreatePayment", response.body())
        return response.body()
    }

    suspend fun createQrisPaymentRequest(request: CreateQrisPaymentRequestRequest): CreateQrisPaymentRequestResponse {
        val session = auth.currentSessionOrNull()
            ?: throw IllegalStateException("No active session")
        val response =  httpClient.post(
            "${BuildConfig.SUPABASE_EDGE_BASE_URL}/qris-payment-close-amount"
        ) {
            contentType(ContentType.Application.Json)
            header("Authorization", "Bearer ${session.accessToken}")
            setBody(request)
        }
        Log.d("CreatePayment", response.body())
        Log.d("CreatePayment", request.toString())
        return response.body()
    }

    @OptIn(SupabaseExperimental::class)
    fun getPaymentAttempt(orderId: String): Flow<PaymentAttemptDto> {
        val result = postgrest.from("payment_attempts")
            .selectSingleValueAsFlow(PaymentAttemptDto::id) {
                PaymentAttemptDto::orderId eq orderId
            }
        return result
    }

    suspend fun getTicketsByOrderId(orderId: String): List<TicketDto> {
        val result = postgrest.from("purchased_tickets")
            .select {
                filter {
                    eq("order_id", orderId)
                }
            }.decodeList<TicketDto>()
        return result
    }

    suspend fun getShipmentByOrderId(orderId: String): ShipmentDto {
        return postgrest.from("shipments").select {
            filter {
                eq("order_id", orderId)
            }
        }.decodeSingle<ShipmentDto>()
    }

    suspend fun getShipmentFees(): List<ShipmentFeeDto> {
        return postgrest.from("shipment_fees").select().decodeList<ShipmentFeeDto>()
    }
}