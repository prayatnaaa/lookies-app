package com.prayatna.lookiesapp.data.remote.api.supabase

import android.util.Log
import com.prayatna.lookiesapp.data.remote.dto.request.order.CreateOrderRpcParams
import com.prayatna.lookiesapp.data.remote.dto.request.order.OrderItemRequest
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.rpc
import javax.inject.Inject

class SupabaseTransactionService @Inject constructor(
    private val auth: Auth,
    private val postgrest: Postgrest
) {

    suspend fun createOrder(
        items: List<OrderItemRequest>
    ): Long {

        val userId = auth.currentUserOrNull()?.id
            ?: throw IllegalStateException("User not authenticated")

        val params = CreateOrderRpcParams(
            buyerId = userId,
            items = items
        )

        val orderId = postgrest.rpc(
            function = "create_order_with_items",
            parameters = params
        ).decodeAs<Long>()

        Log.d("OrderService", "Order created with ID: $orderId")

        return orderId
    }

}