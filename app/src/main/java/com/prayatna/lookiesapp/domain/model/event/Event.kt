package com.prayatna.lookiesapp.domain.model.event

import com.prayatna.lookiesapp.domain.model.merchant.MerchantBusiness

data class Event (
    val id: String,
    val title: String,
    val organizer: MerchantBusiness,
    val bannerImageUrl: String,
    val startDate: String,
    val endDate: String,
    val about: String? = null,
    val location: String,
    val locationUrl: String,
    val maxParticipant: Int? = null,
    val remainingParticipantQuota: Int? = null,
    val remainingPaintingQuota: Int? = null,
    val maxPainting: Int? = null,
    val maxPaintingPerArtist: Int? = null,
    val ticketPrice: Double? = null,
    val artistRegistrationFee: Double? = null,
    val eventType: TEventType,
    val eventFormat: EventFormat,
    val status: String,
    val createdAt: String,
    val updatedAt: String? = null,
    val rejectionReason: String? = null,
    val approvedBy: String? = null,
    val approvedAt: String? = null
)
