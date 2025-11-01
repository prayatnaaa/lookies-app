package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DetailEventDto(

    @SerialName("location_url")
    val locationUrl: String,

    @SerialName("ticket_quantity")
    val ticketQuantity: Int,

    @SerialName("start_time")
    val startTime: String,

    @SerialName("end_time")
    val endTime: String
)
