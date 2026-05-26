package com.prayatna.lookiesapp.data.mapper

import com.prayatna.lookiesapp.data.remote.dto.response.artist.RegisterEventResponse
import com.prayatna.lookiesapp.data.remote.dto.response.artist.SuccessRegisterEventResponse
import com.prayatna.lookiesapp.domain.model.artist.RegisterEventOutput
import com.prayatna.lookiesapp.domain.model.artist.SuccessRegisterEventOutput

fun RegisterEventResponse.toDomain(): RegisterEventOutput {
    return RegisterEventOutput(
        status = status,
        message = message,
        eventId = eventId,
        totalPaintings = totalPaintings,
        data = data?.toDomain()
    )
}

fun SuccessRegisterEventResponse.toDomain() =
    SuccessRegisterEventOutput(
        participantId = participantId,
        totalAmount = totalAmount,
        orderId = orderId

    )