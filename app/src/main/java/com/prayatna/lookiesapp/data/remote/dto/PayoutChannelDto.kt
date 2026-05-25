package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PayoutChannelDto(
    @SerialName("channel_code")
    val channelCode: String,
    @SerialName("channel_category")
    val channelCategory: String,
    val currency: String,
    @SerialName("channel_name")
    val channelName: String,
    @SerialName("amount_limits")
    val amountLimits: AmountLimitsDto
)

@Serializable
data class AmountLimitsDto(
    val minimum: Double,
    val maximum: Double,
    @SerialName("minimum_increment")
    val minimumIncrement: Double
)
