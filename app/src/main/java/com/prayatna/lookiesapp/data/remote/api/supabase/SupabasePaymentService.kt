package com.prayatna.lookiesapp.data.remote.api.supabase

import com.prayatna.lookiesapp.BuildConfig
import com.prayatna.lookiesapp.data.remote.request.payment.AddPaymentRequest
import com.prayatna.lookiesapp.data.remote.response.payment.AddPaymentResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import javax.inject.Inject

class SupabasePaymentService @Inject constructor(
    private val httpClient: HttpClient
) {

    suspend fun addPayment(
        request: AddPaymentRequest,
        token: String
    ): AddPaymentResponse {
        val response: HttpResponse = httpClient.post("${BuildConfig.SUPABASE_EDGE_BASE_URL}/add-payment") {
            contentType(ContentType.Application.Json)
            header("Authorization", "Bearer $token")
            setBody(request)
        }

        if (response.status != HttpStatusCode.OK || !response.status.isSuccess()) {
            val body = response.bodyAsText()
            throw Exception("Failed! ${response.status}\n $body")
        }

        return response.body()
    }
}