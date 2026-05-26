package com.prayatna.lookiesapp.domain.model

import com.prayatna.lookiesapp.domain.model.event.Event
import com.prayatna.lookiesapp.domain.model.user.Profile

data class EventParticipant(
    val id: String,
    val event: Event,
    val artist: Profile,
    val status: String,
)
