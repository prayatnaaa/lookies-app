package com.prayatna.lookiesapp.domain.mapper

import com.prayatna.lookiesapp.data.remote.dto.response.painting.InsertSelfEventPaintingsResponse
import com.prayatna.lookiesapp.domain.model.painting.InsertSelfEventPaintingsResult

fun InsertSelfEventPaintingsResponse.toDomain() =
    InsertSelfEventPaintingsResult (
        id = id
    )