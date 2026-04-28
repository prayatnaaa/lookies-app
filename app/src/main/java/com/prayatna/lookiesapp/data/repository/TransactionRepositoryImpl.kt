package com.prayatna.lookiesapp.data.repository

import android.util.Log
import com.prayatna.lookiesapp.data.mapper.toDomain
import com.prayatna.lookiesapp.data.mapper.toDto
import com.prayatna.lookiesapp.data.remote.api.supabase.SupabaseTransactionService
import com.prayatna.lookiesapp.data.remote.dto.PaymentAttemptDto
import com.prayatna.lookiesapp.data.remote.dto.response.payment.SetOrderToCompleteInput
import com.prayatna.lookiesapp.domain.mapper.toDomain
import com.prayatna.lookiesapp.domain.mapper.toDto
import com.prayatna.lookiesapp.domain.model.order.OrderItemInput
import com.prayatna.lookiesapp.domain.model.payment.PaymentAttempt
import com.prayatna.lookiesapp.domain.model.payment.SetOrderToCompleteResult
import com.prayatna.lookiesapp.domain.model.ticket.Ticket
import com.prayatna.lookiesapp.domain.model.transaction.CreateQrisPaymentRequestInput
import com.prayatna.lookiesapp.domain.model.transaction.CreateQrisPaymentRequestResult
import com.prayatna.lookiesapp.domain.model.transaction.CreateXenditPaymentRequestInput
import com.prayatna.lookiesapp.domain.model.transaction.CreateXenditPaymentRequestResult
import com.prayatna.lookiesapp.domain.model.transaction.Transaction
import com.prayatna.lookiesapp.domain.model.transaction.CreateRefundRequestInput
import com.prayatna.lookiesapp.domain.model.transaction.Refund
import com.prayatna.lookiesapp.domain.model.transaction.SetRefundAsCompleteResult
import com.prayatna.lookiesapp.domain.repository.TransactionRepository
import com.prayatna.lookiesapp.utils.DataResult
import com.prayatna.lookiesapp.utils.extractSupabaseError
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import io.ktor.client.network.sockets.ConnectTimeoutException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val transactionService: SupabaseTransactionService
) : TransactionRepository {
    override suspend fun createOrder(
        items: List<OrderItemInput>,
        shippingCost: Double,
        recipientName: String,
        phoneNumber: String,
        addressLine: String,
        province: String,
        postalCode: String
    ): DataResult<String> {
        return try {
            val response = transactionService.createOrder(
                items = items.map { it.toDto() },
                shippingCost = shippingCost,
                recipientName = recipientName,
                phoneNumber = phoneNumber,
                addressLine = addressLine,
                province = province,
                postalCode = postalCode,
            )
            DataResult.Success(response)
        } catch (e: RestException) {
            Log.e("OrderService", e.error)
            DataResult.Error(e.error)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "Something went wrong")
        }
    }

    override suspend fun createQrisPaymentRequest(data: CreateQrisPaymentRequestInput): DataResult<CreateQrisPaymentRequestResult> {
        return try {
            val response = transactionService.createQrisPaymentRequest(request = data.toDto())
            DataResult.Success(response.toDomain())
        } catch (e: RestException) {
            val eMessage = extractSupabaseError(e.error)
            DataResult.Error(eMessage)
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

    override suspend fun createPaymentRequest(request: CreateXenditPaymentRequestInput): DataResult<CreateXenditPaymentRequestResult> {
        return try {
            val response = transactionService.createPaymentRequest(request = request.toDto())
            if (response.status != "error") {
                DataResult.Success(response.toDomain())
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

    override fun getPaymentAttempt(orderId: String): Flow<DataResult<PaymentAttempt>> {
        return transactionService.getPaymentAttempt(orderId)
            .map<PaymentAttemptDto, DataResult<PaymentAttempt>> { dto ->
                DataResult.Success(dto.toDomain())
            }
            .onStart {
                emit(DataResult.Loading)
            }
            .catch { e ->
                emit(DataResult.Error(e.toString()))
            }
    }

    override suspend fun getTicketsByOrderId(orderId: String): DataResult<List<Ticket>> {
        return try {
            val result = transactionService.getTicketsByOrderId(orderId)
            DataResult.Success(result.map { it.toDomain() })
        } catch (e: RestException) {
            val eMessage = extractSupabaseError(e.error)
            DataResult.Error(eMessage)
        }
    }

    override suspend fun getTransactionByOrderId(orderId: String): DataResult<Transaction> {
        return try {
            val result = transactionService.getUserTransactionByOrderId(orderId)
            DataResult.Success(result.toDomain())
        } catch (e: RestException) {
            val eMessage = extractSupabaseError(e.error)
            DataResult.Error(eMessage)
        }
    }

    override suspend fun setOrderToComplete(request: SetOrderToCompleteInput): DataResult<SetOrderToCompleteResult> {
        return try {
            val result = transactionService.setOrderToComplete(request.toDto())
            DataResult.Success(result.toDomain())
        } catch (e: RestException) {
            val eMessage = extractSupabaseError(e.error)
            DataResult.Error(eMessage)
        }
    }

    override suspend fun createRefundRequest(
        request: CreateRefundRequestInput, 
        proofImage: ByteArray?
    ): DataResult<Refund> {
        return try {
            val result = transactionService.createRefundRequest(request.toDto(), proofImage)
            DataResult.Success(result.toDomain())
        } catch (e: RestException) {
            val eMessage = extractSupabaseError(e.error)
            DataResult.Error(eMessage)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "An unexpected error occurred")
        }
    }

    override suspend fun getRefunds(): DataResult<List<Refund>> {
        return try {
            val result = transactionService.getRefunds()
            DataResult.Success(result.map { it.toDomain() })
        } catch (e: RestException) {
            val eMessage = extractSupabaseError(e.error)
            DataResult.Error(eMessage)
        }
    }

    override suspend fun getRefundsByOrderId(orderId: String): DataResult<List<Refund>> {
        return try {
            val result = transactionService.getRefundsByOrderId(orderId)
            DataResult.Success(result.map { it.toDomain() })
        } catch (e: RestException) {
            val eMessage = extractSupabaseError(e.error)
            DataResult.Error(eMessage)
        }
    }

    override suspend fun setRefundAsComplete(refundRequestId: String): DataResult<SetRefundAsCompleteResult> {
        return try {
            val result = transactionService.setRefundAsComplete(refundRequestId)
            DataResult.Success(result.toDomain())
        } catch (e: RestException) {
            val eMessage = extractSupabaseError(e.error)
            DataResult.Error(eMessage)
        }
    }

    override suspend fun updateRefundStatus(id: String, status: String, note: String?): DataResult<Refund> {
        return try {
            val result = transactionService.updateRefundStatus(id = id, status = status, note)
            DataResult.Success(result.toDomain())
        } catch (e: RestException) {
            val eMessage = extractSupabaseError(e.error)
            DataResult.Error(eMessage)
        }
    }

    override suspend fun getRefundById(id: String): DataResult<Refund> {
        return try {
            val result = transactionService.getRefundById(id)
            DataResult.Success(result.toDomain())
        } catch (e: RestException) {
            val eMessage = extractSupabaseError(e.error)
            DataResult.Error(eMessage)
        }
    }
}