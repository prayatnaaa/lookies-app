package com.prayatna.lookiesapp.data.remote.api.supabase

import android.util.Log
import com.prayatna.lookiesapp.BuildConfig
import com.prayatna.lookiesapp.data.remote.dto.MerchantBalanceLogDto
import com.prayatna.lookiesapp.data.remote.dto.MonthlyFinancialReportDto
import com.prayatna.lookiesapp.data.remote.dto.OrderSplitDto
import com.prayatna.lookiesapp.data.remote.dto.PaymentAttemptDto
import com.prayatna.lookiesapp.data.remote.dto.PendingOrderSplitsDto
import com.prayatna.lookiesapp.data.remote.dto.TicketDto
import com.prayatna.lookiesapp.data.remote.dto.TransactionDto
import com.prayatna.lookiesapp.data.remote.dto.request.order.CreateOrderRpcParams
import com.prayatna.lookiesapp.data.remote.dto.request.order.OrderItemRequest
import com.prayatna.lookiesapp.data.remote.dto.request.payment.CreateQrisPaymentRequestRequest
import com.prayatna.lookiesapp.data.remote.dto.request.payment.CreateXenditPaymentRequest
import com.prayatna.lookiesapp.data.remote.dto.request.payment.SetOrderToCompleteRequest
import com.prayatna.lookiesapp.data.remote.dto.request.transaction.MonthlyFinancialReportFilterRequest
import com.prayatna.lookiesapp.data.remote.dto.request.transaction.PayoutRequest
import com.prayatna.lookiesapp.data.remote.dto.response.payment.CreateQrisPaymentRequestResponse
import com.prayatna.lookiesapp.data.remote.dto.response.payment.CreateXenditPaymentResponse
import com.prayatna.lookiesapp.data.remote.dto.response.payment.SetOrderToCompleteResponse
import com.prayatna.lookiesapp.data.remote.dto.response.transaction.PayoutResponse
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
    private val httpClient: HttpClient,
) {

    suspend fun getPendingOrderSplitByMerchantId(merchantId: String): PendingOrderSplitsDto? {
        return postgrest.rpc(
            "merchant_pending_order_splits",
            mapOf("p_merchant_id" to merchantId)
        ).decodeSingleOrNull<PendingOrderSplitsDto>()
    }

    suspend fun getOrderSplitsByMerchantId(merchantId: String): List<OrderSplitDto> {
        return postgrest.from("order_splits").select {
            filter {
                OrderSplitDto::merchantId eq merchantId
            }
        }.decodeList<OrderSplitDto>()
    }
    suspend fun getMerchantBalanceLogs(merchantId: String): List<MerchantBalanceLogDto> {
        return postgrest.from("merchant_balance_logs").select {
            filter {
                MerchantBalanceLogDto::merchantId eq merchantId
            }

            order(
                column = "created_at",
                order = Order.DESCENDING
            )
        }.decodeList<MerchantBalanceLogDto>()
    }


    suspend fun payouts(withdrawalRequestId: String): PayoutResponse {
        val session = auth.currentSessionOrNull()
            ?: throw IllegalStateException("No active session")

        val response = httpClient.post(
            "${BuildConfig.SUPABASE_EDGE_BASE_URL}/payouts"
        ) {
            contentType(ContentType.Application.Json)
            header("Authorization", "Bearer ${session.accessToken}")
            setBody(PayoutRequest(withdrawalId = withdrawalRequestId))
        }
        Log.d("PayoutService", response.body())
        return response.body()
    }
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
                    neq("status", "awaiting_payment")
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

    suspend fun getOrderDetail(orderId: String): TransactionDto {
        return postgrest
            .from("transactions_view")
            .select {
                filter {
                    eq("order_id", orderId)
                }
                order("created_at", order = Order.DESCENDING)
            }
            .decodeSingle<TransactionDto>()
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

    suspend fun getMonthlyFinancialReport(filter: MonthlyFinancialReportFilterRequest): List<MonthlyFinancialReportDto> {
        return postgrest.rpc("get_monthly_financial_report", filter)
            .decodeList<MonthlyFinancialReportDto>()
    }
}