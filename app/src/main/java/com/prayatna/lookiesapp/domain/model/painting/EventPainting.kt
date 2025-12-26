package com.prayatna.lookiesapp.domain.model.painting

import com.prayatna.lookiesapp.domain.model.EventParticipant

data class EventPainting(
    val id: String,
    val finalPrice: Double,
    val status: String,
    val createdAt: String,
    val painting: Painting,
    val participant: EventParticipant
)