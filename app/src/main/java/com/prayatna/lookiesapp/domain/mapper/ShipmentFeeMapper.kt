package com.prayatna.lookiesapp.domain.mapper

import com.prayatna.lookiesapp.data.remote.dto.ShipmentFeeDto
import com.prayatna.lookiesapp.domain.model.transaction.ShipmentFee

fun ShipmentFeeDto.toDomain() = ShipmentFee(
    id = id,
    fee = fee,
    region = region,
    createdAt = createdAt
)