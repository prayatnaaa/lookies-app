package com.prayatna.lookiesapp.data.mapper

import com.prayatna.lookiesapp.data.remote.dto.response.artist.RegisterEventResponse
import com.prayatna.lookiesapp.domain.model.artist.RegisterEventOutput

fun RegisterEventResponse.toDomain(): RegisterEventOutput {
    return RegisterEventOutput(
        status = status,
        message = message,
        eventId = eventId,
        totalPaintings = totalPaintings,
        totalAmount = totalAmount,
        participantId = participantId,
        orderId = orderId
    )
}