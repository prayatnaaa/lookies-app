package com.prayatna.lookiesapp.data.repository

import android.util.Log
import com.prayatna.lookiesapp.data.mapper.toDomain
import com.prayatna.lookiesapp.data.remote.api.supabase.SupabaseTransactionService
import com.prayatna.lookiesapp.domain.model.transaction.CheckoutOutput
import com.prayatna.lookiesapp.domain.repository.TransactionRepository
import com.prayatna.lookiesapp.utils.DataResult
import io.github.jan.supabase.exceptions.RestException
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val transactionService: SupabaseTransactionService
) : TransactionRepository {
    override suspend fun createOrder(
        userId: String?,
        totalAmount: Double,
        orderType: String,
        description: String?,
        transactionType: String
    ): DataResult<CheckoutOutput> {
        return try {
            val response = transactionService.createOrder(
                userId = userId,
                totalAmount = totalAmount,
                orderType = orderType,
                description = description,
                transactionType = transactionType
            )
            DataResult.Success(response.toDomain())
        } catch (e: RestException) {
            Log.d("TransactionService", "Error: ${e.message}")
            DataResult.Error(e.message ?: "Unknown error")
        }
    }
}