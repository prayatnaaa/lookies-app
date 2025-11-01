package com.prayatna.lookiesapp.data.remote.mapper

import com.prayatna.lookiesapp.domain.model.event.DetailEventInfo
import com.prayatna.lookiesapp.data.remote.response.event.DetailEventResponse

fun DetailEventResponse.asDomainModel(): DetailEventInfo {
    return DetailEventInfo(
        event = event.asDomainModel(),
        detail = detail.asDomainModel(),
    )
}
