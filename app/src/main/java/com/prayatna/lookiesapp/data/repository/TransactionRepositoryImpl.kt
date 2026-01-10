package com.prayatna.lookiesapp.data.repository

import com.prayatna.lookiesapp.data.remote.api.supabase.SupabaseTransactionService
import com.prayatna.lookiesapp.domain.mapper.toDto
import com.prayatna.lookiesapp.domain.model.order.OrderItemInput
import com.prayatna.lookiesapp.domain.repository.TransactionRepository
import com.prayatna.lookiesapp.utils.DataResult
import io.github.jan.supabase.exceptions.RestException
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val transactionService: SupabaseTransactionService
) : TransactionRepository {
    override suspend fun createOrder(
        items: List<OrderItemInput>
    ): DataResult<Long> {
        return try {
            val response = transactionService.createOrder(
                items = items.map { it.toDto() },
            )
            DataResult.Success(response)
        } catch (e: RestException) {
            DataResult.Error(e.error)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "Something went wrong")
        }
    }
}