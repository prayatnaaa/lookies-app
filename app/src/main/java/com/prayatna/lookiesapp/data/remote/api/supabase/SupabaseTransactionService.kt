package com.prayatna.lookiesapp.data.remote.api.supabase

import android.util.Log
import com.prayatna.lookiesapp.data.remote.dto.TransactionDto
import com.prayatna.lookiesapp.data.remote.dto.request.order.CreateOrderRpcParams
import com.prayatna.lookiesapp.data.remote.dto.request.order.OrderItemRequest
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.postgrest.rpc
import javax.inject.Inject

class SupabaseTransactionService @Inject constructor(
    private val auth: Auth,
    private val postgrest: Postgrest
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

        val result = postgrest.from("user_orders_view")
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
}