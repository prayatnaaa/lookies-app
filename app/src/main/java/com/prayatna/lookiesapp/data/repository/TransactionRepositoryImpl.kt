package com.prayatna.lookiesapp.data.repository

import com.prayatna.lookiesapp.data.mapper.toDomain
import com.prayatna.lookiesapp.data.remote.api.supabase.SupabaseTransactionService
import com.prayatna.lookiesapp.data.remote.dto.request.payment.CreateXenditPaymentRequest
import com.prayatna.lookiesapp.data.remote.dto.response.payment.CreateXenditPaymentResponse
import com.prayatna.lookiesapp.domain.mapper.toDto
import com.prayatna.lookiesapp.domain.model.order.OrderItemInput
import com.prayatna.lookiesapp.domain.model.transaction.Transaction
import com.prayatna.lookiesapp.domain.repository.TransactionRepository
import com.prayatna.lookiesapp.utils.DataResult
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import io.ktor.client.network.sockets.ConnectTimeoutException
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val transactionService: SupabaseTransactionService
) : TransactionRepository {
    override suspend fun createOrder(
        items: List<OrderItemInput>
    ): DataResult<String> {
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

    override suspend fun getUserTransactions(): DataResult<List<Transaction>> {
        return try {
            val response = transactionService.getUserTransactions()
            DataResult.Success(response.map { it.toDomain() })
        } catch (e: RestException) {
            DataResult.Error(e.error)
        }
    }

    override suspend fun  createPaymentRequest(request: CreateXenditPaymentRequest): DataResult<CreateXenditPaymentResponse> {
        return try {
            val response = transactionService.createPaymentRequest(request = request)
            if (response.status != "error") {
                DataResult.Success(response)
            } else {
                DataResult.Error(response.message)
            }
        } catch (e: RestException) {
            DataResult.Error(e.error)
        } catch (e: HttpRequestException) {
            DataResult.Error(e.message ?: "Something went wrong!")
        } catch (e: ConnectTimeoutException) {
            DataResult.Error("Connection timeout")
        }
    }
}