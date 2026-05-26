package com.prayatna.lookiesapp.domain.mapper

import com.prayatna.lookiesapp.data.remote.dto.AmountLimitsDto
import com.prayatna.lookiesapp.data.remote.dto.PayoutChannelDto
import com.prayatna.lookiesapp.domain.model.payment.AmountLimits
import com.prayatna.lookiesapp.domain.model.payment.PayoutChannel

fun PayoutChannelDto.toDomain(): PayoutChannel {
    return PayoutChannel(
        channelCode = channelCode,
        channelCategory = channelCategory,
        currency = currency,
        channelName = channelName,
        amountLimits = amountLimits.toDomain()
    )
}

fun AmountLimitsDto.toDomain(): AmountLimits {
    return AmountLimits(
        minimum = minimum,
        maximum = maximum,
        minimumIncrement = minimumIncrement
    )
}
