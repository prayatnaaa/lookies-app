package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExhibitionShipmentDto(

    @SerialName("id")
    val id: String,

    @SerialName("event_painting_id")
    val eventPaintingId: String,

    @SerialName("artist_id")
    val artistId: String,

    @SerialName("organizer_id")
    val organizerId: String,

    @SerialName("shipment_type")
    val shipmentType: String,

    @SerialName("delivery_method")
    val deliveryMethod: String,

    @SerialName("courier_name")
    val courierName: String? = null,

    @SerialName("tracking_number")
    val trackingNumber: String? = null,

    @SerialName("dispatched_at")
    val dispatchedAt: String? = null,

    @SerialName("received_at")
    val receivedAt: String? = null,

    @SerialName("gallery_condition_notes")
    val galleryConditionNotes: String? = null,

    @SerialName("artist_condition_notes")
    val artistConditionNotes: String? = null,

    @SerialName("status")
    val status: String,

    @SerialName("created_at")
    val createdAt: String? = null,

    @SerialName("event_id")
    val eventId: Int,

    @SerialName("arrival_proof_url")
    val arrivalProofUrl: String? = null
)