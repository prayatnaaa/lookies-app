package com.prayatna.lookiesapp.data.remote.dto.response.artist

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterEventResponse(
    val status: String,
    val message: String,
    @SerialName("event_id")
    val eventId: String? = "",
    @SerialName("total_paintings")
    val totalPaintings: String? = "",
    val data: SuccessRegisterEventResponse? = null
)

@Serializable
data class SuccessRegisterEventResponse(
    @SerialName("participant_id")
    val participantId: String,
    @SerialName("total_amount")
    val totalAmount: Int,
    @SerialName("order_id")
    val orderId: String
)