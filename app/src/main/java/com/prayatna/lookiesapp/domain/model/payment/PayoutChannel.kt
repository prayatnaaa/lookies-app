package com.prayatna.lookiesapp.domain.model.payment

data class PayoutChannel(
    val channelCode: String,
    val channelCategory: String,
    val currency: String,
    val channelName: String,
    val amountLimits: AmountLimits
)

data class AmountLimits(
    val minimum: Double,
    val maximum: Double,
    val minimumIncrement: Double
)
