package com.prayatna.lookiesapp.data.remote.api.supabase

import android.util.Log
import com.prayatna.lookiesapp.BuildConfig
import com.prayatna.lookiesapp.data.remote.dto.PaymentAttemptDto
import com.prayatna.lookiesapp.data.remote.dto.RefundDto
import com.prayatna.lookiesapp.data.remote.dto.ShipmentDto
import com.prayatna.lookiesapp.data.remote.dto.ShipmentFeeDto
import com.prayatna.lookiesapp.data.remote.dto.TicketDto
import com.prayatna.lookiesapp.data.remote.dto.TransactionDto
import com.prayatna.lookiesapp.data.remote.dto.request.order.CreateOrderRpcParams
import com.prayatna.lookiesapp.data.remote.dto.request.order.OrderItemRequest
import com.prayatna.lookiesapp.data.remote.dto.request.payment.CreateQrisPaymentRequestRequest
import com.prayatna.lookiesapp.data.remote.dto.request.payment.CreateRefundRequest
import com.prayatna.lookiesapp.data.remote.dto.request.payment.CreateXenditPaymentRequest
import com.prayatna.lookiesapp.data.remote.dto.request.payment.SetOrderToCompleteRequest
import com.prayatna.lookiesapp.data.remote.dto.request.payment.SetRefundAsCompleteRequest
import com.prayatna.lookiesapp.data.remote.dto.response.payment.CreateQrisPaymentRequestResponse
import com.prayatna.lookiesapp.data.remote.dto.response.payment.CreateXenditPaymentResponse
import com.prayatna.lookiesapp.data.remote.dto.response.payment.SetOrderToCompleteResponse
import com.prayatna.lookiesapp.data.remote.dto.response.payment.SetRefundAsCompleteResponse
import com.prayatna.lookiesapp.utils.Helper
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.postgrest.rpc
import io.github.jan.supabase.realtime.selectSingleValueAsFlow
import io.github.jan.supabase.storage.Storage
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject

class SupabaseTransactionService @Inject constructor(
    private val auth: Auth,
    private val postgrest: Postgrest,
    private val httpClient: HttpClient,
    private val storage: Storage
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
            shippingCost = shippingCost
                .toBigDecimal()
                .toLong(),
            recipientName = recipientName,
            phoneNumber = phoneNumber,
            addressLine = addressLine,
            province = province,
            postalCode = postalCode
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

    suspend fun setOrderToComplete(request: SetOrderToCompleteRequest): SetOrderToCompleteResponse {
        val session = auth.currentSessionOrNull()
            ?: throw IllegalStateException("No active session")

        val response =  httpClient.post(
            "${BuildConfig.SUPABASE_EDGE_BASE_URL}/complete-order"
        ) {
            contentType(ContentType.Application.Json)
            header("Authorization", "Bearer ${session.accessToken}")
            setBody(request)
        }
        Log.d("CreatePayment", response.body())
        return response.body()
    }

    suspend fun createRefundRequest(
        request: CreateRefundRequest,
        proofImage: ByteArray?
    ): RefundDto {

        if (proofImage == null) {
            throw IllegalArgumentException("Proof image cannot be null")
        }

        val user = auth.currentUserOrNull() ?: throw IllegalArgumentException("User not logged in")

        var uploadedPath: String? = null

        try {
            // Generate unique storage path
            val path = "refunds/${request.userId}/${UUID.randomUUID()}.png"

            // Upload to Supabase Storage
            storage.from("refund_proof").upload(
                path = path,
                data = proofImage,
                upsert = true
            )

            uploadedPath = path

            // Build public URL
            val proofImageUrl = Helper.buildImageUrl(
                bucketName = "refund_proof",
                imageName = path
            )

            // Attach URL into request
            val finalRequest = request.copy(
                proofImageUrl = proofImageUrl,
                userId = user.id
            )

            // Get session
            val session = auth.currentSessionOrNull()
                ?: throw IllegalStateException("No active session")

            Log.d("Create-Refund", "Access token: ${session.accessToken}")

            // Call Edge Function / API
            val response = postgrest.from("refund_requests")
                .insert(finalRequest) {
                    select()
                }.decodeSingle<RefundDto>()

            Log.d("Create-Refund", response.toString())

            return response

        } catch (e: Exception) {
            uploadedPath?.let {
                storage.from("refund_proof").delete(it)
            }

            throw e
        }
    }

    suspend fun getRefunds(): List<RefundDto> {
        return postgrest.from("refunds").select().decodeList<RefundDto>()
    }

    suspend fun getRefundsByOrderId(orderId: String): List<RefundDto> {
        return postgrest.from("refunds").select {
            filter {
                eq("order_id", orderId)
            }
        }.decodeList<RefundDto>()
    }

    suspend fun setRefundAsComplete(refundRequestId: String): SetRefundAsCompleteResponse {
        val session = auth.currentSessionOrNull()
            ?: throw IllegalStateException("No active session")
        val request = SetRefundAsCompleteRequest(
            refundRequestId = refundRequestId
        )
        val response =  httpClient.post(
            "${BuildConfig.SUPABASE_EDGE_BASE_URL}/process-order"
        ) {
            contentType(ContentType.Application.Json)
            header("Authorization", "Bearer ${session.accessToken}")
            setBody(request)
        }
        Log.d("CreatePayment", response.body())
        return response.body()
    }
}