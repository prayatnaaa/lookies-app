package com.prayatna.lookiesapp.data.repository

import coil.network.HttpException
import com.prayatna.lookiesapp.data.mapper.toDto
import com.prayatna.lookiesapp.data.remote.api.supabase.SupabaseRefundService
import com.prayatna.lookiesapp.domain.mapper.toDomain
import com.prayatna.lookiesapp.domain.model.refund.ProcessRefundOutput
import com.prayatna.lookiesapp.domain.model.transaction.CreateRefundRequestInput
import com.prayatna.lookiesapp.domain.model.transaction.DetailRefund
import com.prayatna.lookiesapp.domain.model.transaction.Refund
import com.prayatna.lookiesapp.domain.model.transaction.SetRefundAsCompleteResult
import com.prayatna.lookiesapp.domain.repository.RefundRepository
import com.prayatna.lookiesapp.utils.DataResult
import com.prayatna.lookiesapp.utils.extractSupabaseError
import io.github.jan.supabase.exceptions.RestException
import javax.inject.Inject

class RefundRepositoryImpl @Inject constructor(
    private val refundService: SupabaseRefundService
): RefundRepository {
    override suspend fun createRefundRequest(
        request: CreateRefundRequestInput,
        proofImage: ByteArray?
    ): DataResult<Refund> {
        return try {
            val result = refundService.createRefundRequest(request.toDto(), proofImage)
            DataResult.Success(result.toDomain())
        } catch (e: RestException) {
            val eMessage = extractSupabaseError(e.error)
            DataResult.Error(eMessage)
        } catch (e: HttpException) {
            val errorMsg = e.response.message
            DataResult.Error(errorMsg)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "An unexpected error occurred")
        }
    }

    override suspend fun getRefunds(): DataResult<List<Refund>> {
        return try {
            val result = refundService.getRefunds()
            DataResult.Success(result.map { it.toDomain() })
        } catch (e: RestException) {
            val eMessage = extractSupabaseError(e.error)
            DataResult.Error(eMessage)
        }  catch (e: HttpException) {
            val errorMsg = e.response.message
            DataResult.Error(errorMsg)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "An unexpected error occurred")
        }
    }

    override suspend fun getRefundsByOrderId(orderId: String): DataResult<List<Refund>> {
        return try {
            val result = refundService.getRefundsByOrderId(orderId)
            DataResult.Success(result.map { it.toDomain() })
        } catch (e: RestException) {
            val eMessage = extractSupabaseError(e.error)
            DataResult.Error(eMessage)
        } catch (e: HttpException) {
            val errorMsg = e.response.message
            DataResult.Error(errorMsg)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "An unexpected error occurred")
        }
    }

    override suspend fun setRefundAsComplete(refundRequestId: String): DataResult<SetRefundAsCompleteResult> {
        return try {
            val result = refundService.setRefundAsComplete(refundRequestId)
            DataResult.Success(result.toDomain())
        } catch (e: RestException) {
            val eMessage = extractSupabaseError(e.error)
            DataResult.Error(eMessage)
        } catch (e: HttpException) {
            val errorMsg = e.response.message
            DataResult.Error(errorMsg)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "An unexpected error occurred")
        }
    }

    override suspend fun updateRefundStatus(id: String, status: String, note: String?): DataResult<Refund> {
        return try {
            val result = refundService.updateRefundStatus(id = id, status = status, note)
            DataResult.Success(result.toDomain())
        } catch (e: RestException) {
            val eMessage = extractSupabaseError(e.error)
            DataResult.Error(eMessage)
        } catch (e: HttpException) {
            val errorMsg = e.response.message
            DataResult.Error(errorMsg)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "An unexpected error occurred")
        }
    }

    override suspend fun getRefundById(id: String): DataResult<DetailRefund> {
        return try {
            val result = refundService.getRefundById(id)
            DataResult.Success(result.toDomain())
        } catch (e: RestException) {
            val eMessage = extractSupabaseError(e.error)
            DataResult.Error(eMessage)
        } catch (e: HttpException) {
            val errorMsg = e.response.message
            DataResult.Error(errorMsg)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "An unexpected error occurred")
        }
    }

    override suspend fun processRefund(refundRequestId: String): DataResult<ProcessRefundOutput> {
        return try {
            val result = refundService.processRefund(refundRequestId)
            if (result.status == "error") {
                return DataResult.Error(result.message)
            }
            DataResult.Success(result.toDomain())
        } catch (e: RestException) {
            val eMessage = extractSupabaseError(e.error)
            DataResult.Error(eMessage)
        } catch (e: HttpException) {
            val errorMsg = e.response.message
            DataResult.Error(errorMsg)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "An unexpected error occurred")
        }
    }

    override suspend fun updateReturnTrackingNumber(id: String, trackingNumber: String): DataResult<Refund> {
        return try {
            val result = refundService.updateReturnTrackingNumber(id, trackingNumber)
            DataResult.Success(result.toDomain())
        } catch (e: RestException) {
            val eMessage = extractSupabaseError(e.error)
            DataResult.Error(eMessage)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "Something went wrong")
        } catch (e: HttpException) {
            val errorMsg = e.response.message
            DataResult.Error(errorMsg)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "An unexpected error occurred")
        }
    }
}
