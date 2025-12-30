package com.prayatna.lookiesapp.data.remote.api.supabase

import android.util.Log
import com.prayatna.lookiesapp.data.remote.dto.request.transaction.CreateOrderRequest
import com.prayatna.lookiesapp.data.remote.dto.response.transaction.CheckoutResponse
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.rpc
import javax.inject.Inject

class SupabaseTransactionService @Inject constructor(
    private val auth: Auth,
    private val postgrest: Postgrest
) {

    suspend fun createOrder(
        userId: String? = null,
        totalAmount: Double,
        orderType: String,
        description: String? = null,
        transactionType: String
    ): CheckoutResponse {

        val finalId = userId ?: auth.currentUserOrNull()?.id
        ?: throw IllegalStateException("User not authenticated")

        val params = CreateOrderRequest(
            userId = finalId,
            totalAmount = totalAmount,
            orderType = orderType,
            description = description,
            transactionType = transactionType
        )

        val response = postgrest.rpc(
            function = "checkout_order",
            parameters = params
        ).decodeAs<CheckoutResponse>()

        Log.d("TransactionService", "Response: $response")

        return response
    }
}