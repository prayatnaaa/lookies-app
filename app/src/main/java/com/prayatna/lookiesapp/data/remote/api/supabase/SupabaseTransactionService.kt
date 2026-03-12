package com.prayatna.lookiesapp.data.remote.api.supabase

import android.util.Log
import com.prayatna.lookiesapp.BuildConfig
import com.prayatna.lookiesapp.data.remote.dto.TransactionDto
import com.prayatna.lookiesapp.data.remote.dto.request.order.CreateOrderRpcParams
import com.prayatna.lookiesapp.data.remote.dto.request.order.OrderItemRequest
import com.prayatna.lookiesapp.data.remote.dto.request.payment.CreateXenditPaymentRequest
import com.prayatna.lookiesapp.data.remote.dto.response.payment.CreateXenditPaymentResponse
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.postgrest.rpc
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject

class SupabaseTransactionService @Inject constructor(
    private val auth: Auth,
    private val postgrest: Postgrest,
    private val httpClient: HttpClient
) {
    suspend fun createOrder(
        items: List<OrderItemRequest>
    ): String {

        val userId = auth.currentUserOrNull()?.id
            ?: throw IllegalStateException("User not authenticated")

        val params = CreateOrderRpcParams(
            buyerId = userId,
            items = items
        )

        val orderId = postgrest.rpc(
            function = "create_order_with_items",
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
}