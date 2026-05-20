package com.prayatna.lookiesapp.domain.model.painting

import com.prayatna.lookiesapp.domain.model.EventParticipant

data class EventPainting(
    val id: String,
    val artistId: String,
    val eventId: String,
    val finalPrice: Double,
    val status: String,
    val createdAt: String,
    val rejectionReason: String? = null,
    val painting: Painting,
    val participant: EventParticipant
)