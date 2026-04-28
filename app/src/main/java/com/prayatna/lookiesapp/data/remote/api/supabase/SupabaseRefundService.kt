package com.prayatna.lookiesapp.data.remote.api.supabase

import android.util.Log
import com.prayatna.lookiesapp.BuildConfig
import com.prayatna.lookiesapp.data.remote.dto.RefundDto
import com.prayatna.lookiesapp.data.remote.dto.request.payment.CreateRefundRequest
import com.prayatna.lookiesapp.data.remote.dto.request.payment.SetRefundAsCompleteRequest
import com.prayatna.lookiesapp.data.remote.dto.request.refund.ProcessRefundRequest
import com.prayatna.lookiesapp.data.remote.dto.response.payment.SetRefundAsCompleteResponse
import com.prayatna.lookiesapp.data.remote.dto.response.refund.ProcessRefundResponse
import com.prayatna.lookiesapp.utils.Helper
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import java.util.UUID
import javax.inject.Inject

class SupabaseRefundService @Inject constructor(
    private val auth: Auth,
    private val postgrest: Postgrest,
    private val httpClient: HttpClient,
    private val storage: Storage
) {
    suspend fun createRefundRequest(
        request: CreateRefundRequest,
        proofImage: ByteArray?
    ): RefundDto {

        if (proofImage == null) {
            throw IllegalArgumentException("Proof image cannot be null")
        }

        val user = auth.currentUserOrNull() ?: throw IllegalArgumentException("User not logged in")

        var uploadedPath: String? = null

        try {
            val path = "refunds/${request.userId}/${UUID.randomUUID()}.png"

            storage.from("refund_proof").upload(
                path = path,
                data = proofImage,
                upsert = true
            )

            uploadedPath = path

            val proofImageUrl = Helper.buildImageUrl(
                bucketName = "refund_proof",
                imageName = path
            )

            val finalRequest = request.copy(
                proofImageUrl = proofImageUrl,
                userId = user.id
            )

            val session = auth.currentSessionOrNull()
                ?: throw IllegalStateException("No active session")

            Log.d("Create-Refund", "Access token: ${session.accessToken}")

            val response = postgrest.from("refund_requests")
                .insert(finalRequest) {
                    select()
                }.decodeSingle<RefundDto>()

            Log.d("Create-Refund", response.toString())

            return response

        } catch (e: Exception) {
            uploadedPath?.let {
                storage.from("refund_proof").delete(it)
            }

            throw e
        }
    }

    suspend fun getRefunds(): List<RefundDto> {
        return postgrest.from("refund_requests").select().decodeList<RefundDto>()
    }

    suspend fun getRefundsByOrderId(orderId: String): List<RefundDto> {
        Log.d("Refund", orderId)
        return postgrest.from("refund_requests").select {
            filter {
                eq("order_id", orderId)
            }
        }.decodeList<RefundDto>()
    }

    suspend fun getRefundById(id: String): RefundDto {
        return postgrest.from("refund_requests").select {
            filter {
                eq("id", id)
            }
        }.decodeSingle<RefundDto>()
    }

    suspend fun setRefundAsComplete(refundRequestId: String): SetRefundAsCompleteResponse {
        val session = auth.currentSessionOrNull()
            ?: throw IllegalStateException("No active session")
        val request = SetRefundAsCompleteRequest(
            refundRequestId = refundRequestId
        )
        val response =  httpClient.post(
            "${BuildConfig.SUPABASE_EDGE_BASE_URL}/process-order"
        ) {
            contentType(ContentType.Application.Json)
            header("Authorization", "Bearer ${session.accessToken}")
            setBody(request)
        }
        Log.d("CreatePayment", response.body())
        return response.body()
    }

    suspend fun updateRefundStatus(id: String, status: String, note: String? = null): RefundDto {
        val refund = postgrest.from("refund_requests").update({
            set("status", status)
            if (note != null) {
                set("admin_notes", note)
            }
        }) {
            select()
            filter {
                eq("id", id)
            }
        }.decodeSingle<RefundDto>()
        Log.d("Refund", refund.toString())
        return refund
    }

    suspend fun processRefund(refundRequestId: String): ProcessRefundResponse {
        val session = auth.currentSessionOrNull()
            ?: throw IllegalStateException("No active session")
        val request = ProcessRefundRequest(
            refundRequestId = refundRequestId
        )
        val response =  httpClient.post(
            "${BuildConfig.SUPABASE_EDGE_BASE_URL}/process-refund"
        ) {
            contentType(ContentType.Application.Json)
            header("Authorization", "Bearer ${session.accessToken}")
            setBody(request)
        }
        Log.d("PROCESS-REFUND", response.body())
        return response.body()
    }
}