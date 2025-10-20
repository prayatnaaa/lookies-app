package com.prayatna.lookiesapp.data.remote.api.supabase

import android.util.Log
import com.prayatna.lookiesapp.BuildConfig
import com.prayatna.lookiesapp.data.remote.dto.DetailEventDto
import com.prayatna.lookiesapp.data.remote.dto.EventDto
import com.prayatna.lookiesapp.data.remote.request.event.AddEventRequest
import com.prayatna.lookiesapp.data.remote.response.event.AddEventResponse
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
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class SupabaseApi @Inject constructor(
    private val httpClient: HttpClient
) {
    suspend fun addEvent(
        token: String,
        event: EventDto,
        detailEvent: DetailEventDto
    ): AddEventResponse {
        val response: HttpResponse = httpClient.post("${BuildConfig.SUPABASE_EDGE_BASE_URL}/create-event") {
            contentType(ContentType.Application.Json)
            header("Authorization", "Bearer $token")
            setBody(AddEventRequest(event, detailEvent))
        }
        Log.d("ADD-EVENT", Json.encodeToString(AddEventRequest(event, detailEvent)))
        if (response.status != HttpStatusCode.OK) {
            val body = response.bodyAsText()
            throw Exception("Failed! ${response.status}\n $body")
        }
        return response.body()
    }
}