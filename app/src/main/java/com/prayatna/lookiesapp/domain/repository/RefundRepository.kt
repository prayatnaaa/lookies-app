package com.prayatna.lookiesapp.domain.repository

import com.prayatna.lookiesapp.domain.model.transaction.CreateRefundRequestInput
import com.prayatna.lookiesapp.domain.model.transaction.Refund
import com.prayatna.lookiesapp.domain.model.transaction.SetRefundAsCompleteResult
import com.prayatna.lookiesapp.utils.DataResult

interface RefundRepository {
    suspend fun createRefundRequest(
        request: CreateRefundRequestInput,
        proofImage: ByteArray?
    ): DataResult<Refund>

    suspend fun getRefunds():
            DataResult<List<Refund>>

    suspend fun getRefundsByOrderId(orderId: String):
            DataResult<List<Refund>>

    suspend fun setRefundAsComplete(refundRequestId: String):
            DataResult<SetRefundAsCompleteResult>

    suspend fun updateRefundStatus(id: String, status: String, note: String? = null):
            DataResult<Refund>
    suspend fun getRefundById(id: String):
            DataResult<Refund>
}